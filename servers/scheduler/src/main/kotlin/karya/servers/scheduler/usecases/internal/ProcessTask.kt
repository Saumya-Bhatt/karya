package karya.servers.scheduler.usecases.internal

import karya.core.entities.Plan
import karya.core.entities.Task
import karya.core.entities.enums.PlanStatus
import karya.core.entities.enums.TaskStatus
import karya.core.exceptions.PlanException.PlanNotFoundException
import karya.core.locks.LocksClient
import karya.core.locks.entities.LockResult
import karya.core.repos.PlansRepo
import karya.core.repos.TasksRepo
import karya.servers.scheduler.usecases.MetricsManager
import karya.servers.scheduler.usecases.utils.getInstanceName
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

/**
 * Use case for processing tasks within a plan.
 *
 * @property tasksRepo The repository for task entities.
 * @property plansRepo The repository for plan entities.
 * @property locksClient The client for managing locks.
 * @property manageTasks The use case for managing tasks.
 * @constructor Creates an instance of [ProcessTask] with the specified dependencies.
 */
class ProcessTask
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val plansRepo: PlansRepo,
  private val locksClient: LocksClient,
  private val manageTasks: ManageTasks,
) {
  companion object : Logging

  /**
   * Invokes the task processing logic for the given task.
   *
   * @param task The task to be processed.
   * @return `true` if the task was processed successfully, `false` otherwise.
   */
  suspend fun invoke(task: Task): Boolean {
    val result = locksClient.withLock(task.id) { processTaskInternal(task) }
    return when (result) {
      is LockResult.Success -> true
      is LockResult.Failure -> false
    }
  }

  /**
   * Internal method to process the task.
   *
   * @param task The task to be processed.
   * @throws PlanNotFoundException If the plan associated with the task is not found.
   */
  private suspend fun processTaskInternal(task: Task) {
    val now = Instant.now()
    logger.info("[${getInstanceName()}] : [PROCESSING TASK] --- TaskId : ${task.id}")
    val plan = plansRepo.get(task.planId) ?: throw PlanNotFoundException(task.planId)
    updateTaskStatus(plan, task)

    val updatedPlan = transitionPlanStatus(plan)
    manageTasks.invoke(updatedPlan, task)
    measureAndRecordProcessLatency(now)
  }

  /**
   * Updates the status of the task based on the plan status.
   *
   * @param plan The plan associated with the task.
   * @param task The task to be updated.
   */
  private suspend fun updateTaskStatus(plan: Plan, task: Task) = when (plan.status) {
    PlanStatus.CREATED, PlanStatus.RUNNING -> TaskStatus.PROCESSING
    PlanStatus.COMPLETED -> TaskStatus.SUCCESS
    PlanStatus.CANCELLED -> TaskStatus.CANCELLED
  }.also {
    if (task.status != it) {
      tasksRepo.updateStatus(task.id, it)
      logger.info("[${getInstanceName()}] : Transitioning task status from : ${task.status} -> $it")
    }
  }

  /**
   * Transitions the status of the plan if necessary.
   *
   * @param plan The plan to be transitioned.
   * @return The updated plan.
   */
  private suspend fun transitionPlanStatus(plan: Plan) = when (plan.status) {
    PlanStatus.CREATED -> plan
      .copy(status = PlanStatus.RUNNING)
      .also { plansRepo.updateStatus(plan.id, PlanStatus.RUNNING) }
      .also { logger.info("[${getInstanceName()}] : Plan Status updated : ${plan.status} -> ${it.status}") }

    else -> plan
  }

  /**
   * Measures the latency of the task processing and records it in the metrics manager.
   *
   * @param startTime The time at which the task processing started.
   */
  private fun measureAndRecordProcessLatency(startTime: Instant) {
    val taskFetchedAt = Instant.now()
    val latency = Duration.between(startTime, taskFetchedAt).toMillis()
    MetricsManager.taskProcessLatencySummary.observe(latency.toDouble())
  }
}
