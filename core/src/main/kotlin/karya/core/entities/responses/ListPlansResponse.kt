package karya.core.entities.responses

import karya.core.entities.Plan
import kotlinx.serialization.Serializable

/**
 * Data class representing the response for listing plans.
 *
 * @property plans The list of plans.
 * @property total The total number of plans.
 * @property offset The offset for pagination.
 */
@Serializable
data class ListPlansResponse(
  val plans: List<Plan>,
  val total: Long,
  val offset: Long,
)
