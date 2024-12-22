package karya.servers.server.api.router

import io.ktor.server.routing.*
import karya.servers.server.api.service.CreateUserService
import karya.servers.server.api.service.GetUserService
import javax.inject.Inject
import javax.inject.Provider

/**
 * Router class for user-related routes.
 *
 * @property createUserService Provider for the CreateUserService.
 * @property getUserService Provider for the GetUserService.
 */
class UserRouter
@Inject
constructor(
  private val createUserService: Provider<CreateUserService>,
  private val getUserService: Provider<GetUserService>
) {
  /**
   * Wires the user-related routes.
   *
   * @receiver The Route to which the routes will be added.
   */
  fun Route.wireRoutes() {
    get { getUserService.get().invoke(call) }
    post { createUserService.get().invoke(call) }
  }
}
