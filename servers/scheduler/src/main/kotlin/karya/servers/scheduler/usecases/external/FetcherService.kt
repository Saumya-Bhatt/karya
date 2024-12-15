package karya.servers.scheduler.usecases.external

import karya.core.entities.Task
import karya.core.entities.enums.TaskStatus
import karya.core.repos.TasksRepo
import karya.core.repos.entities.GetTasksRequest
import karya.servers.scheduler.app.SchedulerManager
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.usecases.MetricsManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

/**
 * Service class responsible for fetching tasks and sending them to a channel.
 *
 * @property tasksRepo The repository for task entities.
 * @property config The configuration for the scheduler.
 * @constructor Creates an instance of [FetcherService] with the specified dependencies.
 */
class FetcherService
@Inject
constructor(
  private val tasksRepo: TasksRepo,
  private val config: SchedulerConfig,
) {
  companion object : Logging

  /**
   * Invokes the fetcher service to fetch tasks and send them to the given channel.
   *
   * @param taskChannel The channel to which fetched tasks are sent.
   */
  suspend fun invoke(taskChannel: Channel<Task>) {
    while (true) {
      if (SchedulerManager.isStopped.get()) return
      val task = getOpenTask()
      if (task != null) {
        taskChannel.send(task)
        measureAndRecordLatency(task)
      } else {
        logger.info("Poller fetched 0 tasks...")
      }
      delay(config.pollFrequency)
    }
  }

  /**
   * Retrieves an open task from the repository.
   *
   * @return The open task if available, `null` otherwise.
   */
  private suspend fun getOpenTask(): Task? = GetTasksRequest(
    partitionKeys = config.partitions,
    executionTime = Instant.now(),
    buffer = Duration.ofMillis(config.executionBufferInMilli),
    status = TaskStatus.CREATED,
  ).let { tasksRepo.get(it) }
    ?.also { tasksRepo.updateStatus(it.id, TaskStatus.PROCESSING) }

  /**
    * Measures the latency of the task fetch and records it in the metrics manager.
    *
    * @param task The task that was fetched.
   */
  private fun measureAndRecordLatency(task: Task) {
    val taskSupposedToHappenAt = task.nextExecutionAt?.let { Instant.ofEpochMilli(it) } ?: return
    val taskFetchedAt = Instant.now()
    val latency = Duration.between(taskSupposedToHappenAt, taskFetchedAt).toMillis()
    MetricsManager.taskFetchLatencySummary.observe(latency.toDouble())
  }
}
