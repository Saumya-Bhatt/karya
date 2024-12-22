package karya.data.psql.repos.plans

import karya.core.entities.Plan
import karya.core.entities.enums.PlanStatus
import karya.core.repos.PlansRepo
import karya.data.psql.repos.plans.mappers.PlanStatusMapper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*
import javax.inject.Inject

class PsqlPlansRepo
@Inject
constructor(
  private val db: Database,
  private val planStatusMapper: PlanStatusMapper,
) : PlansRepo {

  override suspend fun add(plan: Plan) {
    transaction(db) {
      PlansTable.insert {
        it[id] = plan.id
        it[userId] = plan.userId
        it[description] = plan.description
        it[periodTime] = plan.periodTime
        it[type] = plan.type
        it[status] = planStatusMapper.toRecord(plan.status)
        it[maxFailureRetry] = plan.maxFailureRetry
        it[action] = plan.action
        it[hook] = plan.hook
        it[parentPlanId] = plan.parentPlanId
        it[createdAt] = Instant.ofEpochMilli(plan.createdAt)
        it[updatedAt] = Instant.ofEpochMilli(plan.updatedAt)
      }
    }
  }

  override suspend fun get(id: UUID): Plan? = transaction(db) {
    PlansTable
      .selectAll()
      .where { PlansTable.id eq id }
      .firstOrNull()
  }?.let(::fromRecord)

  override suspend fun update(plan: Plan) {
    transaction(db) {
      PlansTable.update({ PlansTable.id eq plan.id }) {
        it[status] = planStatusMapper.toRecord(plan.status)
        it[periodTime] = plan.periodTime
        it[action] = plan.action
        it[maxFailureRetry] = plan.maxFailureRetry
        it[updatedAt] = Instant.ofEpochMilli(plan.updatedAt)
      }
    }
  }

  override suspend fun updateStatus(id: UUID, status: PlanStatus) {
    transaction(db) {
      PlansTable.update({ PlansTable.id eq id }) {
        it[PlansTable.status] = planStatusMapper.toRecord(status)
        it[updatedAt] = Instant.now()
      }
    }
  }

  override suspend fun getChildPlanIds(id: UUID): List<UUID> = transaction(db) {
    val query = """
            WITH RECURSIVE ChildPlanIds AS (
                SELECT id FROM plans WHERE parent_plan_id = '$id'
                UNION ALL
                SELECT p.id FROM plans p INNER JOIN ChildPlanIds cp ON p.parent_plan_id = cp.id
            )
            SELECT id FROM ChildPlanIds;
        """.trimIndent()

    exec(query, explicitStatementType = StatementType.SELECT) { resultSet ->
      val ids = mutableListOf<UUID>()
      while (resultSet.next()) {
        ids.add(UUID.fromString(resultSet.getString("id")))
      }
      ids
    } ?: emptyList()
  }

  override suspend fun getAll(userId: UUID): List<Plan> = transaction(db) {
    PlansTable
      .selectAll()
      .where { PlansTable.userId eq userId }
      .map(::fromRecord)
  }

  private fun fromRecord(resultRow: ResultRow) = Plan(
    id = resultRow[PlansTable.id],
    userId = resultRow[PlansTable.userId],
    description = resultRow[PlansTable.description],
    periodTime = resultRow[PlansTable.periodTime],
    type = resultRow[PlansTable.type],
    status = planStatusMapper.fromRecord(resultRow[PlansTable.status]),
    maxFailureRetry = resultRow[PlansTable.maxFailureRetry],
    action = resultRow[PlansTable.action],
    hook = resultRow[PlansTable.hook],
    parentPlanId = resultRow[PlansTable.parentPlanId],
    createdAt = resultRow[PlansTable.createdAt].toEpochMilli(),
    updatedAt = resultRow[PlansTable.updatedAt].toEpochMilli(),
  )
}
