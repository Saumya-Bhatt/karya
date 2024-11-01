package karya.core.repos

import karya.core.entities.User
import java.util.UUID

interface UsersRepo {
  suspend fun add(user: User)
  suspend fun get(id: UUID): User?
}