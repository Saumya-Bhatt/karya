package karya.core.entities.requests

import karya.core.entities.Hook
import karya.core.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UpdatePlanRequest(
  @Serializable(with = UUIDSerializer::class)
  val planId: UUID,
  val periodTime: String? = null,
  val maxFailureRetry: Int? = null,
  val hooks: List<Hook>? = null
)