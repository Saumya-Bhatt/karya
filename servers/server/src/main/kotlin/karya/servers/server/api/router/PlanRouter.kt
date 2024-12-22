package karya.servers.server.api.router

import io.ktor.server.routing.*
import karya.servers.server.api.service.*
import javax.inject.Inject
import javax.inject.Provider

/**
 * Router class responsible for wiring plan-related routes.
 *
 * @property submitPlanService Provider for the SubmitPlanService.
 * @property getPlanService Provider for the GetPlanService.
 * @property updatePlanService Provider for the UpdatePlanService.
 * @property cancelPlanService Provider for the CancelPlanService.
 * @property getSummaryService Provider for the GetSummaryService.
 */
@Suppress("LongParameterList")
class PlanRouter
@Inject
constructor(
  private val submitPlanService: Provider<SubmitPlanService>,
  private val getPlanService: Provider<GetPlanService>,
  private val updatePlanService: Provider<UpdatePlanService>,
  private val cancelPlanService: Provider<CancelPlanService>,
  private val getSummaryService: Provider<GetSummaryService>,
  private val listPlansService: Provider<ListPlansService>
) {
  /**
   * Wires the routes for plan-related operations.
   *
   * Defines the PATCH route for updating a plan, the POST route for submitting a plan,
   * and nested routes for getting a plan, getting a plan summary, and canceling a plan.
   */
  fun Route.wireRoutes() {

    get { listPlansService.get().invoke(call) }
    patch { updatePlanService.get().invoke(call) }
    post { submitPlanService.get().invoke(call) }

    route("{plan_id}") {
      get { getPlanService.get().invoke(call) }
      get("summary") { getSummaryService.get().invoke(call) }
      post { cancelPlanService.get().invoke(call) }
    }
  }
}
