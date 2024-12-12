package karya.data.sqs.usecases

import karya.core.queues.entities.QueueMessage
import karya.core.utils.decodeFromString
import karya.data.sqs.SqsQueueClient
import karya.data.sqs.configs.QueueConfig
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.Logging
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import javax.inject.Inject

class ConsumeMessage
@Inject
constructor(
  private val json: Json,
  private val sqsClient: SqsClient,
  private val config: QueueConfig
) {

  companion object: Logging

  suspend fun invoke(queueUrl: String, onMessage: suspend (QueueMessage) -> Unit) {
    if(!SqsQueueClient.isRunning.get()) return
    val request = buildRequest(queueUrl)
    val response = sqsClient.receiveMessage(request)
    response.messages().forEach { message -> processAndDeleteMessage(message, queueUrl, onMessage) }
  }

  private fun buildRequest(queueUrl: String) = ReceiveMessageRequest.builder()
    .queueUrl(queueUrl)
    .maxNumberOfMessages(config.maxMessageFetched)
    .waitTimeSeconds(config.longPollingWaitTime)
    .visibilityTimeout(config.visibilityTimeout)
    .build()

  private suspend fun processAndDeleteMessage(
    message: Message,
    queueUrl: String,
    onMessage: suspend (QueueMessage) -> Unit
  ) {
    try {
      val queueMessage = message.body().decodeFromString(json)
      onMessage(queueMessage)

      sqsClient.deleteMessage(
        DeleteMessageRequest.builder()
          .queueUrl(queueUrl)
          .receiptHandle(message.receiptHandle())
          .build()
      )
    } catch (e: Exception) {
      logger.error(e) { "Error processing message: ${e.message}" }
      // In case of failure, message will return to queue based on visibility timeout
    }
  }
}
