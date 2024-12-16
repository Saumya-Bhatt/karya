package karya.core.entities.responses

import karya.core.entities.ErrorLog
import karya.core.entities.Plan
import karya.core.entities.Task
import kotlinx.serialization.Serializable

/**
 * Data class representing the response for getting a summary.
 *
 * @property plan The plan associated with the response.
 * @property tasks The list of tasks associated with the plan.
 * @property errorLogs The list of error logs associated with the plan.
 */
@Serializable
data class GetSummaryResponse(
  val plan: Plan,
  val tasks: List<Task>,
  val errorLogs: List<ErrorLog>
)
