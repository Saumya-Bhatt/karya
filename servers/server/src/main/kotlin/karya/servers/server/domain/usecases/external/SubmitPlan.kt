package karya.servers.server.domain.usecases.external

import karya.core.entities.Plan
import karya.core.entities.Task
import karya.core.entities.User
import karya.core.entities.enums.PlanStatus
import karya.core.entities.enums.TaskStatus
import karya.core.entities.requests.SubmitPlanRequest
import karya.core.exceptions.UserException
import karya.core.repos.PlansRepo
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.repos.UsersRepo
import karya.core.usecases.createPartitionKey
import karya.core.usecases.getNextExecutionAt
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Use case for submitting a plan.
 *
 * @property usersRepo The repository for accessing users.
 * @property plansRepo The repository for accessing plans.
 * @property tasksRepo The repository for accessing tasks.
 * @property repoConnector The connector for accessing repository partitions.
 */
class SubmitPlan
@Inject
constructor(
  private val usersRepo: UsersRepo,
  private val plansRepo: PlansRepo,
  private val tasksRepo: TasksRepo,
  private val repoConnector: RepoConnector,
) {

  companion object : Logging

  /**
   * Submits a plan based on the given request and parent plan ID.
   *
   * @param request The request to submit a plan.
   * @param parentPlanId The ID of the parent plan, if any.
   * @return The submitted plan.
   * @throws UserException.UserNotFoundException If the user is not found.
   */
  suspend fun invoke(request: SubmitPlanRequest, parentPlanId: UUID?): Plan = usersRepo
    .get(request.userId)
    ?.let { createPlan(request, it, parentPlanId) }
    ?.also { createTask(it) }
    ?: throw UserException.UserNotFoundException(request.userId.toString())

  /**
   * Creates a plan based on the given request, user, and parent plan ID.
   *
   * @param request The request to create a plan.
   * @param user The user associated with the plan.
   * @param parentPlanId The ID of the parent plan, if any.
   * @return The created plan.
   */
  private suspend fun createPlan(request: SubmitPlanRequest, user: User, parentPlanId: UUID?) = Plan(
    id = UUID.randomUUID(),
    userId = user.id,
    description = request.description,
    periodTime = request.periodTime,
    type = request.planType,
    status = PlanStatus.CREATED,
    maxFailureRetry = request.maxFailureRetry,
    action = request.action,
    parentPlanId = parentPlanId,
    hook = request.hooks,
    createdAt = Instant.now().toEpochMilli(),
    updatedAt = Instant.now().toEpochMilli(),
  ).also { plansRepo.add(it) }
    .also { logger.info("[PLAN CREATED] --- $it") }

  /**
   * Creates a task for the given plan.
   *
   * @param plan The plan for which to create a task.
   * @return The created task.
   */
  private suspend fun createTask(plan: Plan) = Task(
    id = UUID.randomUUID(),
    planId = plan.id,
    partitionKey = createPartitionKey(repoConnector.getPartitions()),
    status = TaskStatus.CREATED,
    createdAt = Instant.now().toEpochMilli(),
    executedAt = null,
    nextExecutionAt = getNextExecutionAt(Instant.ofEpochMilli(plan.createdAt), plan.periodTime),
  ).also { tasksRepo.add(it) }
    .also { logger.info("[TASK CREATED] --- $it") }
}
