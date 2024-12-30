package karya.servers.server.domain.usecases.external

import com.github.benmanes.caffeine.cache.Caffeine
import karya.core.entities.responses.ListPlanResponse
import karya.core.repos.PlansRepo
import java.util.*
import java.util.concurrent.TimeUnit
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
    const val SIZE = 1  // The number of plans to retrieve in a single request.
    private val countCache = Caffeine.newBuilder()
      .expireAfterWrite(30, TimeUnit.SECONDS)
      .build<UUID, Long>()
  }

  /**
   * Retrieves all plans for a given user.
   *
   * @param userId The ID of the user whose plans are to be retrieved.
   * @param page The offset from which to retrieve the plans.
   * @return A response containing a list of plans for the specified user, the total count of plans, and the offset.
   */
  suspend fun invoke(userId: UUID, page: Long): ListPlanResponse {
    val plans = plansRepo.getAllPaginate(userId, page, SIZE)
    val count = getPlanCount(userId)
    return ListPlanResponse(plans, count, page)
  }

  /**
   * Retrieves the total count of plans for a given user, using a cache to minimize database queries.
   *
   * @param userId The ID of the user whose plan count is to be retrieved.
   * @return The total count of plans for the specified user.
   */
  private suspend fun getPlanCount(userId: UUID): Long {
    val cachedCount = countCache.getIfPresent(userId)
    return cachedCount ?: run {
      val countFromDb = plansRepo.getAllCount(userId)
      countCache.put(userId, countFromDb)
      countFromDb
    }
  }
}
