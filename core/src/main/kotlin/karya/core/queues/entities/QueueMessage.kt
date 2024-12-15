package karya.core.queues.entities

import karya.core.entities.Action
import karya.core.entities.Hook
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Sealed class representing different types of queue messages.
 */
@Serializable
sealed class QueueMessage {

  /**
   * Data class representing a message for the executor.
   *
   * @property planId The unique identifier of the plan.
   * @property taskId The unique identifier of the task.
   * @property action The action to be performed.
   * @property maxFailureRetry The maximum number of retries allowed on failure.
   */
  @Serializable
  data class ExecutorMessage(
    @Serializable(with = UUIDSerializer::class)
    val planId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val taskId: UUID,
    val action: Action,
    val maxFailureRetry: Int,
    val toBeExecutedAt: Long
  ) : QueueMessage()

  /**
   * Data class representing a message for the hook.
   *
   * @property planId The unique identifier of the plan.
   * @property hook The hook to be executed.
   */
  @Serializable
  data class HookMessage(
    @Serializable(with = UUIDSerializer::class)
    val planId: UUID,
    val hook: Hook
  ) : QueueMessage()

}
