package karya.data.rabbitmq

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import karya.core.queues.QueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.data.rabbitmq.configs.ExchangeConfig.DL_EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.DL_ROUTING_KEY
import karya.data.rabbitmq.configs.ExchangeConfig.EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.EXECUTOR_QUEUE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.EXECUTOR_ROUTING_KEY
import karya.data.rabbitmq.configs.ExchangeConfig.HOOKS_ROUTING_KEY
import karya.data.rabbitmq.usecases.external.InitializeConfiguration
import karya.data.rabbitmq.usecases.external.RabbitMqConsumer
import karya.data.rabbitmq.usecases.internal.MessageEncoder
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class RabbitMqQueueClient
@Inject
constructor(
  private val channel: Channel,
  private val connection: Connection,
  private val consumer: RabbitMqConsumer,
  private val messageEncoder: MessageEncoder,
  private val initializeConfiguration: InitializeConfiguration
) : QueueClient {

  companion object : Logging {
    private const val CONTENT_TYPE = "application/json"
    private const val DELIVERY_MODE = 2 // persistent
    private const val PRIORITY = 1
  }

  override suspend fun initialize() {
    initializeConfiguration.invoke()
  }

  override suspend fun push(message: QueueMessage, queueType: QueueType) {
    try {
      val messageBytes = messageEncoder.encode(message)
      val properties = buildProperties()
      val (exchangeToUse, routingKeyToUse) = provideExchangeAndRoutingKey(queueType)
      channel.basicPublish(exchangeToUse, routingKeyToUse, properties, messageBytes)
      logger.info("[MESSAGE PUSHED : $message] --- pushed to $exchangeToUse : $message")

    } catch (e: Exception) {
      logger.error("Error pushing message to RabbitMQ: ${e.message}", e)
      throw e
    }
  }

  // will maintain a persistent connection so no need for polling
  override suspend fun consume(onMessage: suspend (QueueMessage) -> Unit) {
    consumer.onMessage = onMessage
    channel.basicConsume(EXECUTOR_QUEUE_NAME, false, consumer)
    logger.info("Started consuming messages from queue: $EXECUTOR_QUEUE_NAME")
  }

  override suspend fun shutdown(): Boolean = try {
    channel.close()
    connection.close()
    logger.info("RabbitMqQueueClient shutdown successfully")
    true
  } catch (e: Exception) {
    logger.error("Error shutting down RabbitMqQueueClient --- $e")
    false
  }

  private fun provideExchangeAndRoutingKey(queueType: QueueType): Pair<String, String> = when (queueType) {
    QueueType.EXECUTOR -> Pair(EXCHANGE_NAME, EXECUTOR_ROUTING_KEY)
    QueueType.DEAD_LETTER -> Pair(DL_EXCHANGE_NAME, DL_ROUTING_KEY)
    QueueType.HOOK -> Pair(EXCHANGE_NAME, HOOKS_ROUTING_KEY)
  }

  private fun buildProperties(): BasicProperties = BasicProperties
    .Builder()
    .contentType(CONTENT_TYPE)
    .deliveryMode(DELIVERY_MODE) // persistent
    .priority(PRIORITY)
    .build()
}
