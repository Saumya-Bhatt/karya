package karya.client.ktor

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import karya.client.ktor.utils.deserialize
import karya.core.actors.Client
import karya.core.entities.Plan
import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.entities.requests.UpdatePlanRequest
import karya.core.entities.responses.GetPlanResponse
import karya.core.entities.responses.GetSummaryResponse
import kotlinx.serialization.json.Json
import java.util.*

/**
 * Implementation of the Client interface using Ktor HTTP client.
 *
 * @property httpClient The Ktor HTTP client instance.
 * @property json The JSON serializer instance.
 */
class KaryaClientImpl(
  private val httpClient: HttpClient,
  private val json: Json,
) : Client {

  companion object {
    private const val VERSION = "v1"
    private const val PLAN = "plan"
    private const val USER = "user"
  }

  /**
   * Creates a new user.
   *
   * @param request The request object containing user details.
   * @return The created User object.
   */
  override suspend fun createUser(request: CreateUserRequest): User = httpClient
    .post {
      url { path(VERSION, USER) }
      setBody(request)
    }.deserialize<User>(json)

  /**
   * Get user
   *
   * @param username
   * @return The user corresponding to the username
   */
  override suspend fun getUser(username: String): User = httpClient
    .get {
      url {
        path(VERSION, USER)
        parameters.append("username", username)
      }
    }.deserialize<User>(json)

  /**
   * Submits a new plan.
   *
   * @param request The request object containing plan details.
   * @return The submitted Plan object.
   */
  override suspend fun submitPlan(request: SubmitPlanRequest): Plan = httpClient
    .post {
      url { path(VERSION, PLAN) }
      setBody(request)
    }.deserialize<Plan>(json)

  /**
   * Retrieves a plan by its ID.
   *
   * @param planId The UUID of the plan.
   * @return The response object containing plan details.
   */
  override suspend fun getPlan(planId: UUID): GetPlanResponse = httpClient
    .get {
      url { path(VERSION, PLAN, planId.toString()) }
    }.deserialize<GetPlanResponse>(json)

  /**
   * Updates an existing plan.
   *
   * @param request The request object containing updated plan details.
   * @return The updated Plan object.
   */
  override suspend fun updatePlan(request: UpdatePlanRequest): Plan = httpClient
    .patch {
      url { path(VERSION, PLAN) }
      setBody(request)
    }.deserialize<Plan>(json)

  /**
   * Cancels a plan by its ID.
   *
   * @param planId The UUID of the plan.
   * @return The canceled Plan object.
   */
  override suspend fun cancelPlan(planId: UUID): Plan = httpClient
    .post {
      url { path(VERSION, PLAN, planId.toString()) }
    }.deserialize<Plan>(json)

  /**
   * Retrieves the summary of a plan by its ID.
   *
   * @param planId The UUID of the plan.
   * @return The response object containing plan summary details.
   */
  override suspend fun getSummary(planId: UUID): GetSummaryResponse = httpClient
    .get {
      url { path(VERSION, PLAN, planId.toString(), "summary") }
    }.deserialize<GetSummaryResponse>(json)

  override suspend fun listPlans(userId: UUID): List<Plan> = httpClient
    .get {
      url {
        path(VERSION, PLAN)
        parameters.append("user_id", userId.toString())
      }
    }.deserialize<List<Plan>>(json)

  /**
   * Closes the HTTP client.
   */
  override suspend fun close() {
    httpClient.close()
  }
}
