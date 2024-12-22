package karya.data.psql.repos.users

import karya.core.entities.User
import karya.core.repos.UsersRepo
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import javax.inject.Inject

class PsqlUsersRepo
@Inject
constructor(
  private val db: Database,
) : UsersRepo {

  override suspend fun add(user: User) {
    transaction(db) {
      UsersTable.insert {
        it[id] = user.id
        it[name] = user.name
        it[createdAt] = Instant.ofEpochMilli(user.createdAt)
      }
    }
  }

  override suspend fun get(id: UUID): User? = transaction(db) {
    UsersTable
      .selectAll()
      .where { UsersTable.id eq id }
      .firstOrNull()
  }?.let(::fromRecord)

  override suspend fun getByName(username: String): User? = transaction(db) {
    UsersTable
      .selectAll()
      .where { UsersTable.name eq username }
      .firstOrNull()
  }?.let(::fromRecord)

  private fun fromRecord(resultRow: ResultRow) = User(
    id = resultRow[UsersTable.id],
    name = resultRow[UsersTable.name],
    createdAt = resultRow[UsersTable.createdAt].toEpochMilli(),
  )
}
