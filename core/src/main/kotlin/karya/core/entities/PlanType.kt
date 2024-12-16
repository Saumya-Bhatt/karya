package karya.core.entities

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Sealed class representing the type of plan.
 */
@Serializable
sealed class PlanType {

  /**
   * Data class representing a recurring plan type.
   *
   * @property endAt The end time of the recurring plan in milliseconds since epoch. Pass null to make it infinite.
   */
  @Serializable
  data class Recurring(val endAt: Long?) : PlanType() {
    /**
     * Checks if the recurring plan has ended.
     *
     * @return True if the plan has ended, false otherwise.
     */
    fun isEnded(): Boolean =
      endAt?.let { Instant.now().toEpochMilli() >= it } == true
  }

  /**
   * Object representing a one-time plan type.
   */
  @Serializable
  object OneTime : PlanType()
}
