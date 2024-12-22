package karya.servers.server.api.router

import io.ktor.server.routing.*
import karya.servers.server.api.service.CreateUserService
import karya.servers.server.api.service.GetUserService
import javax.inject.Inject
import javax.inject.Provider

class UserRouter
@Inject
constructor(
  private val createUserService: Provider<CreateUserService>,
  private val getUserService: Provider<GetUserService>
) {
  fun Route.wireRoutes() {
    get { getUserService.get().invoke(call) }
    post { createUserService.get().invoke(call) }
  }
}
