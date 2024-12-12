package karya.core.utils

import karya.core.queues.entities.QueueMessage
import kotlinx.serialization.json.Json

/**
 * Extension function to encode a QueueMessage to a ByteArray using the provided Json instance.
 *
 * @param json The Json instance used for encoding.
 * @return The encoded ByteArray.
 */
fun QueueMessage.encodeToByteArray(json: Json): ByteArray =
  json.encodeToString(QueueMessage.serializer(), this)
    .toByteArray(Charsets.UTF_8)

/**
 * Extension function to encode a QueueMessage to a String using the provided Json instance.
 *
 * @param json The Json instance used for encoding.
 * @return The encoded String.
 */
fun QueueMessage.encodeToString(json: Json): String =
  json.encodeToString(QueueMessage.serializer(), this)

/**
 * Extension function to decode a ByteArray to a QueueMessage using the provided Json instance.
 *
 * @param json The Json instance used for decoding.
 * @return The decoded QueueMessage.
 * @throws IllegalArgumentException if the ByteArray is null.
 */
fun ByteArray?.decodeFromByteArray(json: Json): QueueMessage {
  val messageJson = this?.toString(Charsets.UTF_8)
    ?: throw IllegalArgumentException("Message body is null")
  return json.decodeFromString<QueueMessage>(messageJson)
}

/**
 * Extension function to decode a String to a QueueMessage using the provided Json instance.
 *
 * @param json The Json instance used for decoding.
 * @return The decoded QueueMessage.
 * @throws IllegalArgumentException if the String is null.
 */
fun String?.decodeFromString(json: Json): QueueMessage {
  val messageJson = this ?: throw IllegalArgumentException("Message body is null")
  return json.decodeFromString<QueueMessage>(messageJson)
}
