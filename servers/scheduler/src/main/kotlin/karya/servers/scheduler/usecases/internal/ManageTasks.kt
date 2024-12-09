package karya.servers.scheduler.usecases.internal

import karya.core.entities.Plan
import karya.core.entities.Task
import karya.core.entities.enums.PlanStatus
import karya.core.entities.enums.TaskStatus
import karya.core.queues.ProducerQueueClient
import karya.core.queues.entities.QueueMessage.ExecutorMessage
import karya.core.repos.RepoConnector
import karya.core.repos.TasksRepo
import karya.core.usecases.createPartitionKey
import karya.core.usecases.getNextExecutionAt
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Use case for managing tasks within a plan.
 *
 * @property tasksRepo The repository for task entities.
 * @property repoConnector The connector for repository interactions.
 * @property queueClient The producer client for interacting with the queue.
 * @property shouldCreateNextTask The use case to determine if the next task should be created.
 * @constructor Creates an instance of [ManageTasks] with the specified dependencies.
 */
class ManageTasks
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val repoConnector: RepoConnector,
  private val queueClient: ProducerQueueClient,
  private val shouldCreateNextTask: ShouldCreateNextTask
) {
  companion object : Logging

  /**
   * Invokes the task management process for the given plan and task.
   *
   * @param plan The plan associated with the task.
   * @param task The task to be managed.
   */
  suspend fun invoke(plan: Plan, task: Task) {
    if (isPlanTerminated(plan)) return
    pushCurrentTaskToQueue(plan, task)
    if (shouldCreateNextTask.invoke(plan)) createNextTask(plan)
  }

  /**
   * Pushes the current task to the queue for execution.
   *
   * @param plan The plan associated with the task.
   * @param task The task to be pushed to the queue.
   */
  private suspend fun pushCurrentTaskToQueue(plan: Plan, task: Task) = ExecutorMessage(
    planId = plan.id,
    taskId = task.id,
    action = plan.action,
    maxFailureRetry = plan.maxFailureRetry,
  ).also { queueClient.push(it) }

  /**
   * Creates the next task in the plan.
   *
   * @param plan The plan for which the next task is to be created.
   */
  private suspend fun createNextTask(plan: Plan) = Task(
    id = UUID.randomUUID(),
    planId = plan.id,
    partitionKey = createPartitionKey(repoConnector.getPartitions()),
    status = TaskStatus.CREATED,
    createdAt = Instant.now().toEpochMilli(),
    executedAt = null,
    nextExecutionAt = getNextExecutionAt(Instant.now(), plan.periodTime),
  ).also { tasksRepo.add(it) }
    .also { logger.info("[${getInstanceName()}] : [NEXT TASK CREATED] --- $it") }

  /**
   * Checks if the plan is terminated.
   *
   * @param plan The plan to be checked.
   * @return `true` if the plan is terminated, `false` otherwise.
   */
  private fun isPlanTerminated(plan: Plan) =
    (plan.status == PlanStatus.COMPLETED).or(plan.status == PlanStatus.CANCELLED)
}
