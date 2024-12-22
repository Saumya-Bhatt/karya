package karya.servers.server.domain.usecases.external

import karya.core.entities.User
import karya.core.exceptions.UserException
import karya.core.repos.UsersRepo
import javax.inject.Inject

/**
 * Use case for retrieving a user by username.
 *
 * @property usersRepo Repository for accessing user data.
 */
class GetUser
@Inject
constructor(
  private val usersRepo: UsersRepo
) {
  /**
   * Retrieves a user by their username.
   *
   * @param username The username of the user to retrieve.
   * @return The user with the specified username.
   * @throws UserException.UserNotFoundException if the user is not found.
   */
  suspend fun invoke(username: String): User =
    usersRepo.getByName(username) ?: throw UserException.UserNotFoundException(username)
}
