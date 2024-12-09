package karya.core.queues

import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.queues.entities.QueueType.EXECUTOR

/**
 * Interface representing a producer client for managing queue operations.
 */
interface ProducerQueueClient {

  /**
   * Initializes the queue producer client.
   */
  suspend fun initialize()

  /**
   * Pushes a message to the specified queue.
   *
   * @param message The message to be pushed to the queue.
   * @param queueType The type of the queue to which the message will be pushed. Defaults to [QueueType.EXECUTOR].
   */
  suspend fun push(message: QueueMessage, queueType: QueueType = EXECUTOR)

  /**
   * Shuts down the queue client.
   *
   * @return `true` if the shutdown was successful, `false` otherwise.
   */
  suspend fun shutdown(): Boolean
}
