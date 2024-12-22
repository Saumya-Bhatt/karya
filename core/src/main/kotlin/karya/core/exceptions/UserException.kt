package karya.core.exceptions

import java.util.*

/**
 * Sealed class representing exceptions related to users.
 */
sealed class UserException : KaryaException() {

  /**
   * Exception thrown when a user is not found.
   *
   * @property userId The unique identifier of the user that was not found.
   * @property message The error message.
   */
  data class UserNotFoundException(
    private val userId: String,
    override val message: String = "User ($userId) not found",
  ) : UserException()
}
