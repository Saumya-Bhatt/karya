package karya.servers.server.domain.usecases.external

import karya.core.entities.User
import karya.core.exceptions.UserException
import karya.core.repos.UsersRepo
import javax.inject.Inject

class GetUser
@Inject
constructor(
  private val usersRepo: UsersRepo
) {
  suspend fun invoke(username: String): User =
    usersRepo.getByName(username) ?: throw UserException.UserNotFoundException(username)
}