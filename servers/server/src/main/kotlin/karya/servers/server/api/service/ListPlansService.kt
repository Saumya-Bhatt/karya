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

class ListPlansService
@Inject
constructor(
  private val listPlans: ListPlans
) {

  companion object : Logging

  suspend fun invoke(call: ApplicationCall) = try {
    val userId = UUID.fromString(getUserId(call))
    val response = listPlans.invoke(userId)
    call.respond(HttpStatusCode.OK, response)

  } catch (e: KaryaException) {
    logger.error(e)
    e.toHttpResponse(call)
  }

  private fun getUserId(call: ApplicationCall) =
    call.request.queryParameters["user_id"] ?: throw UserException.UserNotFoundException("")
}