package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.http.Body
import karya.core.entities.http.Method
import karya.core.entities.http.Protocol
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.entities.requests.UpdatePlanRequest
import java.time.Instant

/**
 * Main function to demonstrate making a periodic API call.
 */
suspend fun main() {
  // Create a Karya client instance using the development configuration
  val client = KaryaClientFactory.create(KaryaClientConfig.Dev)
  // Create a new user with the name "Alice"
  val user = client.createUser(CreateUserRequest("Alice"))

  // Define a REST API request action
  val apiRequest = Action.RestApiRequest(
    protocol = Protocol.HTTPS,
    baseUrl = "eox7wbcodh9parh.m.pipedream.net",
    method = Method.POST,
    headers = mapOf(
      "content-type" to "application/json",
      "client-header" to "Alice",
    ),
    body = Body.JsonBody(
      data = mapOf(
        "udf1" to "value",
        "udf2" to 1,
        "udf3" to true,
        "udf4" to 1.2,
        "udf5" to listOf(1, 2, 3, 4),
        "udf6" to mapOf(
          "nested-udf1" to listOf("a", "b", "c"),
          "nested-udf2" to mapOf("nested-nested-udf1" to true),
        ),
        "udf7" to listOf(
          mapOf("nested-udf3" to listOf(1, 2, 3)),
        ),
      ),
    ),
    timeout = 1000L,
  )

  // Create a plan request with the defined API request action
  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Sample run",
    periodTime = "PT7S",
    planType = PlanType.Recurring(Instant.now().plusSeconds(30).toEpochMilli()),
    action = apiRequest
  )

  // Submit the plan request and print the result
  val plan = client.submitPlan(planRequest)
  client.getPlan(plan.id).also(::println)

  // Update the plan with a maximum failure retry count
  client.updatePlan(
    UpdatePlanRequest(
      planId = plan.id,
      maxFailureRetry = 4,
    ),
  ).also(::println)

  // Optionally cancel a plan by its UUID
  // client.cancelPlan(UUID.fromString("b92a1108-a16d-4b6c-b33a-e7ebf284b613")).also(::println)

  // Close the client
  client.close()
}
