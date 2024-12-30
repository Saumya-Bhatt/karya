package karya.core.actors

import karya.core.entities.Plan
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.entities.responses.GetPlanResponse
import karya.core.entities.responses.GetSummaryResponse
import java.util.*

/**
 * Interface representing a client that can perform various operations related to users and plans.
 */
interface Client {

  /**
   * Creates a new user based on the provided request.
   *
   * @param request The request containing the details for creating a new user.
   * @return The created user.
   */
  suspend fun createUser(request: CreateUserRequest): User

  /**
   * Get user by username
   *
   * @param username
   * @return The corresponding user
   */
  suspend fun getUser(username: String): User

  /**
   * Submits a new plan based on the provided request.
   *
   * @param request The request containing the details for submitting a new plan.
   * @return The submitted plan.
   */
  suspend fun submitPlan(request: SubmitPlanRequest): Plan

  /**
   * Retrieves the details of a plan based on the provided plan ID.
   *
   * @param planId The unique identifier of the plan.
   * @return The response containing the details of the plan.
   */
  suspend fun getPlan(planId: UUID): GetPlanResponse

  /**
   * Updates an existing plan based on the provided request.
   *
   * @param request The request containing the details for updating the plan.
   * @return The updated plan.
   */
  suspend fun updatePlan(request: UpdatePlanRequest): Plan

  /**
   * Cancels a plan based on the provided plan ID.
   *
   * @param planId The unique identifier of the plan.
   * @return The canceled plan.
   */
  suspend fun cancelPlan(planId: UUID): Plan

  /**
   * Retrieves a summary of the plan based on the provided plan ID.
   *
   * @param planId The unique identifier of the plan.
   * @return The response containing the summary of the plan.
   */
  suspend fun getSummary(planId: UUID): GetSummaryResponse

  /**
   * List plans submitted by a user
   *
   * @param userId The ID of the user whose plans are to be retrieved
   * @param page The offset (from 0) from which to retrieve the plans
   * @return The list of plans the user submitted
   */
  suspend fun listPlans(userId: UUID, page: Long): List<Plan>

  /**
   * Closes the client and releases any resources held by it.
   */
  suspend fun close()
}
