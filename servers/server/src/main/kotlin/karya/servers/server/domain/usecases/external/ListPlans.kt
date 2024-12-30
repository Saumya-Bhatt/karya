package karya.servers.server.domain.usecases.external

import karya.core.entities.Plan
import karya.core.repos.PlansRepo
import java.util.*
import javax.inject.Inject

/**
 * Use case for listing plans for a user.
 *
 * @property plansRepo Repository for accessing plan data.
 */
class ListPlans
@Inject
constructor(
  private val plansRepo: PlansRepo
) {


  companion object {
    const val SIZE = 20  // The number of plans to retrieve in a single request.
  }

  /**
   * Retrieves all plans for a given user.
   *
   * @param userId The ID of the user whose plans are to be retrieved.
   * @param page The offset from which to retrieve the plans.
   * @return A list of plans for the specified user.
   */
  suspend fun invoke(userId: UUID, page: Long): List<Plan> =
    plansRepo.getAll(userId, page, SIZE)
}
