package karya.servers.server.domain.usecases.external

import karya.core.entities.Plan
import karya.core.repos.PlansRepo
import java.util.*
import javax.inject.Inject

class ListPlans
@Inject
constructor(
  private val plansRepo: PlansRepo
) {

  suspend fun invoke(userId: UUID): List<Plan> =
    plansRepo.getAll(userId)
}