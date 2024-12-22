package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import karya.core.exceptions.KaryaException
import karya.core.exceptions.UserException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.GetUser
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Service class for getting user details.
 *
 * @property getUser Use case for getting user details.
 */
class GetUserService
@Inject
constructor(
  private val getUser: GetUser
) {

  companion object : Logging

  /**
   * Invokes the service to get user details.
   *
   * @param call The application call.
   */
  suspend fun invoke(call: ApplicationCall) = try {
    val username = getUsername(call)
    val response = getUser.invoke(username)
    call.respond(HttpStatusCode.OK, response)

  } catch (e: KaryaException) {
    logger.error(e)
    e.toHttpResponse(call)
  }

  /**
   * Retrieves the username from the request query parameters.
   *
   * @param call The application call.
   * @return The username as a string.
   * @throws UserException.UserNotFoundException if the username is not found in the query parameters.
   */
  private fun getUsername(call: ApplicationCall) =
    call.request.queryParameters["username"] ?: throw UserException.UserNotFoundException("")
}
