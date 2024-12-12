package karya.data.sqs

import karya.core.queues.ConsumerQueueClient
import karya.core.queues.ProducerQueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.utils.encodeToString
import karya.data.sqs.configs.QueueConfig
import karya.data.sqs.usecases.CheckIfQueueCreated
import karya.data.sqs.usecases.ConsumeMessage
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class SqsQueueClient
@Inject
constructor(
  private val json: Json,
  private val sqsClient: SqsClient,
  private val config: QueueConfig,
  private val consumeMessage: ConsumeMessage,
  private val checkIfQueueCreated: CheckIfQueueCreated
) : ProducerQueueClient, ConsumerQueueClient {

  companion object : Logging {
    val isRunning = AtomicBoolean(true)

    private const val EXECUTOR_QUEUE_ID = "executor"
    private const val HOOKS_QUEUE_ID = "hooks"
    private const val DEAD_LETTER_QUEUE_ID = "dead-letter"
  }

  override suspend fun initialize() {
    checkIfQueueCreated.invoke(config.hooksQueueUrl)
    checkIfQueueCreated.invoke(config.executorQueueUrl)
    checkIfQueueCreated.invoke(config.deadLetterQueueUrl)
  }

  override suspend fun push(message: QueueMessage, queueType: QueueType) {
    val queueUrl = getQueueUrl(queueType)
    val sendRequest = SendMessageRequest.builder()
      .queueUrl(queueUrl)
      .messageBody(message.encodeToString(json))
      .messageGroupId(getMessageQueueId(queueType))
      .build()

    try {
      sqsClient.sendMessage(sendRequest)
      logger.info("[MESSAGE PUSHED : $message] --- pushed to $queueUrl")

    } catch (e: Exception) {
      logger.error(e) { "Error pushing message to SQS: ${e.message}" }
      throw e
    }
  }

  override suspend fun consume(onMessage: suspend (QueueMessage) -> Unit) {
    val functionalQueues = listOf(config.executorQueueUrl, config.hooksQueueUrl)
    try {
      while (isRunning.get()) {
        functionalQueues.forEach { queueUrl ->
          consumeMessage.invoke(queueUrl, onMessage)
        }
      }

    } catch (e: Exception) {
      logger.error(e) { "Consumption error: ${e.message}" }

    } finally {
      isRunning.set(false)
    }
  }

  override suspend fun shutdown(): Boolean {
    return try {
      isRunning.set(false)
      delay(config.longPollingWaitTime * 60L)
      sqsClient.close()
      true
    } catch (e: Exception) {
      logger.error(e) { "Error during shutdown: ${e.message}" }
      false
    }
  }

  private fun getQueueUrl(type: QueueType) = when (type) {
    QueueType.EXECUTOR -> config.executorQueueUrl
    QueueType.HOOK -> config.hooksQueueUrl
    QueueType.DEAD_LETTER -> config.deadLetterQueueUrl
  }

  private fun getMessageQueueId(type: QueueType) = when (type) {
    QueueType.EXECUTOR -> EXECUTOR_QUEUE_ID
    QueueType.HOOK -> HOOKS_QUEUE_ID
    QueueType.DEAD_LETTER -> DEAD_LETTER_QUEUE_ID
  }
}
