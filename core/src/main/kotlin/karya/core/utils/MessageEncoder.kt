package karya.core.utils

import karya.core.queues.entities.QueueMessage
import kotlinx.serialization.json.Json

fun QueueMessage.encodeToByteArray(json: Json): ByteArray =
  json.encodeToString(QueueMessage.serializer(), this)
    .toByteArray(Charsets.UTF_8)

fun QueueMessage.encodeToString(json: Json): String =
  json.encodeToString(QueueMessage.serializer(), this)

fun ByteArray?.decodeFromByteArray(json: Json): QueueMessage {
  val messageJson = this?.toString(Charsets.UTF_8)
    ?: throw IllegalArgumentException("Message body is null")
  return json.decodeFromString<QueueMessage>(messageJson)
}

fun String?.decodeFromString(json: Json): QueueMessage {
  val messageJson = this ?: throw IllegalArgumentException("Message body is null")
  return json.decodeFromString<QueueMessage>(messageJson)
}
