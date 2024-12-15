package karya.servers.executor.usecase

import karya.core.queues.ConsumerQueueClient
import karya.core.queues.ProducerQueueClient
import karya.core.repos.RepoConnector
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.usecase.external.ProcessMessage
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.kotlin.Logging
import javax.inject.Inject

/**
 * Service class responsible for managing the executor service.
 *
 * @property repoConnector The connector for repository interactions.
 * @property config The configuration for the executor service.
 * @property processMessage The use case for processing messages.
 * @property producerClient The producer client for interacting with the queue.
 * @property consumerClient The consumer client for interacting with the queue.
 * @constructor Creates an instance of [ExecutorService] with the specified dependencies.
 */
class ExecutorService
@Inject
constructor(
  private val repoConnector: RepoConnector,
  private val config: ExecutorConfig,
  private val processMessage: ProcessMessage,
  private val producerClient: ProducerQueueClient,
  private val consumerClient: ConsumerQueueClient
) {

  companion object : Logging

  /**
   * Starts the executor service by initializing the queue client and consuming messages.
   */
  suspend fun start() {
    logger.info("Starting executor service...")
    if (config.metricsEnabled) MetricsManager.startMetrics()
    producerClient.initialize()
    consumerClient.consume { message -> processMessage.invoke(message) }
  }

  /**
   * Stops the executor service by shutting down the queue client, repository connector, and connectors.
   */
  fun stop() = runBlocking {
    logger.info("Shutting down executor service...")
    producerClient.shutdown()
    consumerClient.shutdown()
    repoConnector.shutdown()
    config.connectors.forEach { action, connector ->
      runBlocking {
        connector.shutdown()
        logger.info("Connector for action [$action] shutdown complete.")
      }
    }
    if (config.metricsEnabled) MetricsManager.stopMetrics()
    logger.info("Executor service shutdown complete.")
  }
}
