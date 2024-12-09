package karya.servers.executor.usecase.external

import karya.core.entities.ErrorLog
import karya.core.entities.ErrorLogType
import karya.core.entities.ExecutorResult
import karya.core.queues.ProducerQueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.ErrorLogsRepo
import karya.servers.executor.usecase.internal.GetConnector
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

/**
 * Use case class responsible for processing hook messages.
 *
 * @property getConnector The use case for retrieving connectors.
 * @property queueClient The producer client for interacting with the queue.
 * @property errorLogsRepo The repository for managing error logs.
 * @constructor Creates an instance of [ProcessHookMessage] with the specified dependencies.
 */
class ProcessHookMessage
@Inject
constructor(
  private val getConnector: GetConnector,
  private val queueClient: ProducerQueueClient,
  private val errorLogsRepo: ErrorLogsRepo
) {

  companion object : Logging

  /**
   * Processes the hook message.
   *
   * @param message The hook message to be processed.
   */
  suspend fun invoke(message: QueueMessage.HookMessage) {
    val connector = getConnector.invoke(message.hook.action)
    val result = connector.invoke(message.planId, message.hook.action)
    when (result) {
      is ExecutorResult.Success -> logger.info("[HOOK ASYNC] --- hook processed successfully")
      is ExecutorResult.Failure -> if (shouldRetryFailedHook(message, result)) retryHook(message)
    }
  }

  /**
   * Determines if the failed hook should be retried.
   *
   * @param message The hook message containing the hook details.
   * @param result The failed executor result.
   * @return True if the hook should be retried, false otherwise.
   */
  private suspend fun shouldRetryFailedHook(message: QueueMessage.HookMessage, result: ExecutorResult.Failure) =
    (message.hook.maxRetry > 0)
      .also { pushErrorLogs(message, result) }
      .also { logger.info("[RETRY HOOK ASYNC | maxRetry : ${message.hook.maxRetry}] --- result : $result") }

  /**
   * Retries the failed hook by pushing it back to the queue.
   *
   * @param message The hook message to be retried.
   */
  private suspend fun retryHook(message: QueueMessage.HookMessage) {
    val updatedHook = message.hook.copy(maxRetry = message.hook.maxRetry - 1)
    val updatedMessage = message.copy(hook = updatedHook)
    queueClient.push(updatedMessage, QueueType.HOOK)
  }

  /**
   * Pushes error logs to the repository.
   *
   * @param message The hook message containing the hook details.
   * @param result The failed executor result.
   */
  private suspend fun pushErrorLogs(message: QueueMessage.HookMessage, result: ExecutorResult.Failure) = ErrorLog(
    planId = message.planId,
    error = result.reason,
    type = ErrorLogType.HookErrorLog,
    timestamp = result.timestamp
  ).let { errorLogsRepo.push(it) }
}
