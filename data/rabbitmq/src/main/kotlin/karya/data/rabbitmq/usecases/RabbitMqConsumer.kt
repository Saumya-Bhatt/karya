package karya.data.rabbitmq.usecases

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import karya.core.queues.entities.QueueMessage
import karya.core.utils.decodeFromByteArray
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

class RabbitMqConsumer
@Inject
constructor(
  private val json: Json,
  private val channel: Channel
) : DefaultConsumer(channel) {

  companion object : Logging

  var onMessage: (suspend (QueueMessage) -> Unit)? = null

  override fun handleDelivery(
    consumerTag: String?,
    envelope: Envelope?,
    properties: BasicProperties?,
    body: ByteArray?,
  ) {
    envelope?.let {
      try {
        val message = body.decodeFromByteArray(json)
        onMessage?.let { handler -> runBlocking { handler(message) } }
        channel.basicAck(envelope.deliveryTag, false)

      } catch (e: Exception) {
        channel.basicNack(envelope.deliveryTag, false, true) // Requeue message on failure
        logger.error("Error processing message: ${e.message}", e)
      }
    } ?: logger.error("Received null envelope, message discarded.")
  }
}
