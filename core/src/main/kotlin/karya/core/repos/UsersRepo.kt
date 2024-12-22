package karya.core.repos

import karya.core.entities.User
import java.util.*

/**
 * Interface representing a repository for managing users.
 */
interface UsersRepo {

  /**
   * Adds a new user to the repository.
   *
   * @param user The user to be added.
   */
  suspend fun add(user: User)

  /**
   * Retrieves a user by their unique identifier.
   *
   * @param id The unique identifier of the user.
   * @return The user associated with the specified identifier, or `null` if not found.
   */
  suspend fun get(id: UUID): User?

  suspend fun getByName(username: String): User?
}
