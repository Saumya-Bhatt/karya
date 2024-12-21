package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import java.util.UUID

suspend fun main() {

  val planId = UUID.fromString("fa35c473-4ee5-4d8f-b214-06b9edadfc70")

  // Configuration for the Karya client
  val config = KaryaClientConfig.Dev
  // Create a Karya client instance
  val client = KaryaClientFactory.create(config)

  // Cancel a plan by its UUID
  client.cancelPlan(planId)

  // Get the plan by its UUID
  val plan = client.getPlan(planId)
  println(plan.plan.status)
}
