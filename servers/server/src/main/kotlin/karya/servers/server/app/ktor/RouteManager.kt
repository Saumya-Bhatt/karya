package karya.servers.server.app.ktor

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import karya.servers.server.api.router.PlanRouter
import karya.servers.server.api.router.UserRouter
import javax.inject.Inject

/**
 * Class responsible for managing the routes in the Ktor server.
 *
 * @property userRouter The router for user-related routes.
 * @property planRouter The router for plan-related routes.
 */
class RouteManager
@Inject
constructor(
  private val userRouter: UserRouter,
  private val planRouter: PlanRouter,
) {

  /**
   * Wires the routes to the Ktor application.
   *
   * @receiver The Ktor application instance.
   */
  fun Application.wireRoutes(meterRegistry: PrometheusMeterRegistry) {
    routing {

      // Responds with HTTP 200 OK for the root path
      get { call.respond(HttpStatusCode.OK, Unit) }

      options("{...}") {
        call.response.header("Access-Control-Allow-Origin", "*")
        call.response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        call.response.header("Access-Control-Allow-Headers", "Content-Type, Authorization")
        call.respond(HttpStatusCode.OK)
      }

      // Prometheus metrics will be exposed here automatically by Micrometer
      get("/metrics") {
        call.respond(meterRegistry.scrape())
      }

      // Defines the version 1 API routes
      route("v1") {
        // Wires the user-related routes
        route("user") { userRouter.apply { wireRoutes() } }
        // Wires the plan-related routes
        route("plan") { planRouter.apply { wireRoutes() } }
      }
    }
  }
}
