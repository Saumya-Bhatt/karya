package karya.core.queues

import karya.core.queues.entities.QueueMessage

/**
 * Interface representing a consumer client for managing queue operations.
 */
interface ConsumerQueueClient {

  /**
   * Consumes messages from the queue and processes them using the provided handler.
   *
   * @param onMessage The handler function to process each consumed message.
   */
  suspend fun consume(onMessage: suspend (QueueMessage) -> Unit)

  /**
   * Shuts down the queue client.
   *
   * @return `true` if the shutdown was successful, `false` otherwise.
   */
  suspend fun shutdown(): Boolean
}
