package karya.servers.scheduler.usecases

import karya.core.entities.Task
import karya.core.locks.LocksClient
import karya.core.queues.ProducerQueueClient
import karya.core.repos.RepoConnector
import karya.servers.scheduler.usecases.external.WorkerService
import karya.servers.scheduler.usecases.utils.withNamedContext
import kotlinx.coroutines.channels.ReceiveChannel
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Scheduler worker responsible for starting and stopping the task processing.
 *
 * @property workerService The service used to process tasks.
 * @property repoConnector The repository connector for managing database connections.
 * @property locksClient The client for managing distributed locks.
 * @property queueClient The producer client for managing task queues.
 * @constructor Creates an instance of [SchedulerWorker] with the specified dependencies.
 */
class SchedulerWorker
@Inject
constructor(
  private val workerService: WorkerService,
  private val repoConnector: RepoConnector,
  private val locksClient: LocksClient,
  private val queueClient: ProducerQueueClient,
) {
  companion object : Logging

  /**
   * Starts the worker service to begin processing tasks.
   *
   * @param instanceId The ID of the worker instance.
   * @param channel The receive channel from which tasks are consumed.
   */
  suspend fun start(instanceId: Int, channel: ReceiveChannel<Task>) {
    logger.info("Starting worker instance $instanceId...")
    withNamedContext("scheduler-karya-worker-$instanceId") { workerService.invoke(channel) }
    logger.info("Worker Instance $instanceId started.")
  }

  /**
   * Stops the worker service and shuts down the repository connector, locks client, and queue client.
   *
   * @param instanceId The ID of the worker instance.
   */
  suspend fun stop(instanceId: Int) {
    logger.info("Shutting down worker instance $instanceId...")
    repoConnector.shutdown()
    locksClient.shutdown()
    queueClient.shutdown()
    logger.info("Worker Instance $instanceId shutdown complete.")
  }
}
