package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.http.Protocol
import karya.core.entities.requests.CreateUserRequest
import karya.core.entities.requests.SubmitPlanRequest

suspend fun main() {
  val config = KaryaClientConfig.Dev
  val client = KaryaClientFactory.create(config)

  val user = client.createUser(CreateUserRequest("alice")).also(::println)

  val emailRequest = Action.EmailRequest(
    recipient = "recipient@gmail.com",
    subject = "Karya notification",
    message = "Hello from Karya!"
  )

  // Create a plan request with the defined email request action
  val planRequest = SubmitPlanRequest(
    userId = user.id,
    description = "Sample delay email run",
    periodTime = "PT5S",
    planType = PlanType.OneTime,
    action = emailRequest
  )

  client.submitPlan(planRequest)
}