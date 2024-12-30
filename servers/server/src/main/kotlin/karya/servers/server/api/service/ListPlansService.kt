package karya.servers.server.api.service

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import karya.core.exceptions.KaryaException
import karya.core.exceptions.UserException
import karya.servers.server.api.mapper.toHttpResponse
import karya.servers.server.domain.usecases.external.ListPlans
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

/**
 * Service class for listing plans.
 *
 * @property listPlans Use case for listing plans.
 */
class ListPlansService
@Inject
constructor(
  private val listPlans: ListPlans
) {

  companion object : Logging

  /**
   * Invokes the service to list plans for a user.
   *
   * @param call The application call.
   */
  suspend fun invoke(call: ApplicationCall) = try {
    val userId = UUID.fromString(getUserId(call))
    val page = call.request.queryParameters["page"]?.toLong() ?: 0
    val response = listPlans.invoke(userId, page)
    call.respond(HttpStatusCode.OK, response)

  } catch (e: KaryaException) {
    logger.error(e)
    e.toHttpResponse(call)
  }

  /**
   * Retrieves the user ID from the request query parameters.
   *
   * @param call The application call.
   * @return The user ID as a string.
   * @throws UserException.UserNotFoundException if the user ID is not found in the query parameters.
   */
  private fun getUserId(call: ApplicationCall) =
    call.request.queryParameters["user_id"] ?: throw UserException.UserNotFoundException("")
}
