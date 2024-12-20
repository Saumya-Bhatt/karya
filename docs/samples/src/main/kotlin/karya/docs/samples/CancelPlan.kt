package karya.docs.samples

import karya.client.configs.KaryaClientConfig
import karya.client.di.KaryaClientFactory
import java.util.UUID

suspend fun main() {
  // Configuration for the Karya client
  val config = KaryaClientConfig.Dev
  // Create a Karya client instance
  val client = KaryaClientFactory.create(config)

  // Cancel a plan by its UUID
  client.cancelPlan(UUID.fromString("8532da0a-fe1d-44da-9676-5f77dbc7cced"))

  // Get the plan by its UUID
  val plan = client.getPlan(UUID.fromString("8532da0a-fe1d-44da-9676-5f77dbc7cced"))
  println(plan.plan.status)
}
