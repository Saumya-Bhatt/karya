package karya.data.rabbitmq.usecases

import com.rabbitmq.client.Channel
import karya.data.rabbitmq.configs.ExchangeConfig.DL_EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.DL_QUEUE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.DL_ROUTING_KEY
import karya.data.rabbitmq.configs.ExchangeConfig.EXCHANGE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.EXCHANGE_TYPE
import karya.data.rabbitmq.configs.ExchangeConfig.EXECUTOR_QUEUE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.EXECUTOR_ROUTING_KEY
import karya.data.rabbitmq.configs.ExchangeConfig.HOOKS_QUEUE_NAME
import karya.data.rabbitmq.configs.ExchangeConfig.HOOKS_ROUTING_KEY
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class InitializeConfiguration
@Inject
constructor(
  private val channel: Channel
) {

  companion object : Logging

  fun invoke() = try {
    declareFunctionalExchange(EXECUTOR_QUEUE_NAME, EXECUTOR_ROUTING_KEY)
    declareFunctionalExchange(HOOKS_QUEUE_NAME, HOOKS_ROUTING_KEY)
    declareDeadLetterExchange()

  } catch (e: Exception) {
    logger.error("Error during RabbitMQ initialization: ${e.message}", e)
    throw e
  }

  private fun declareFunctionalExchange(queueName: String, routingKey: String) {
    channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true)
    channel.queueDeclare(
      queueName, true, false, false, mapOf(
        "x-dead-letter-exchange" to DL_EXCHANGE_NAME
      )
    )
    channel.queueBind(queueName, EXCHANGE_NAME, routingKey)
    logger.info("RabbitMQ exchange [$EXCHANGE_NAME : $queueName] initialized.")
  }

  private fun declareDeadLetterExchange() {
    channel.exchangeDeclare(DL_EXCHANGE_NAME, EXCHANGE_TYPE, true)
    channel.queueDeclare(DL_QUEUE_NAME, true, false, false, null)
    channel.queueBind(DL_QUEUE_NAME, DL_EXCHANGE_NAME, DL_ROUTING_KEY)
    logger.info("RabbitMQ exchange [$DL_EXCHANGE_NAME] initialized.")
  }
}
