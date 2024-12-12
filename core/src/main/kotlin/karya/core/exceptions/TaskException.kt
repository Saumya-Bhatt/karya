package karya.core.exceptions

import java.util.*

/**
 * Sealed class representing exceptions related to tasks.
 */
sealed class TaskException : KaryaException() {

  /**
   * Exception thrown when a task cannot be created for a specific plan.
   *
   * @property planId The unique identifier of the plan for which the task could not be created.
   * @property message The error message.
   */
  data class TaskNotCreatedException(
    private val planId: UUID,
    override val message: String = "Task not created for Plan --- $planId",
  ) : TaskException()

  /**
   * Exception thrown when an unknown task status is encountered.
   *
   * @property taskStatusId The identifier of the unknown task status.
   * @property message The error message.
   */
  data class UnknownTaskStatusException(
    private val taskStatusId: Int,
    override val message: String = "Unknown Task Status ID --- $taskStatusId",
  ) : TaskException()
}
