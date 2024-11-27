package karya.servers.scheduler.app

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.factories.SchedulerFetcherFactory
import karya.servers.scheduler.di.factories.SchedulerWorkerFactory
import karya.servers.scheduler.usecases.SchedulerWorker
import kotlinx.coroutines.*
import org.apache.logging.log4j.kotlin.Logging
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class SchedulerManager(
  private val config: SchedulerConfig,
) {

  private val dispatcher = Executors.newFixedThreadPool(config.threadCount).asCoroutineDispatcher()
  private val scope = CoroutineScope(dispatcher + SupervisorJob())
  private val failedWorkerCount = AtomicInteger(0)

  private val fetcher = SchedulerFetcherFactory.build(config)
  private val workers = List(config.workers) { SchedulerWorkerFactory.build(config) }

  companion object : Logging {
    val isStopped = AtomicBoolean(false)
  }

  fun start() {
    runBlocking {
      scope.launch(fetcherErrorHandler) { fetcher.start() }
      workers.forEachIndexed { index, worker ->
        scope.launch(workerErrorHandlerFor(index, worker)) { worker.start(index, fetcher.taskReadChannel) }
      }
    }
  }

  fun stop() {
    if (isStopped.compareAndSet(false, true)) {
      runBlocking {
        coroutineScope {
          fetcher.stop()
          workers.forEachIndexed { index, worker -> worker.stop(index) }
        }
        dispatcher.close()
        logger.info { "Scheduler manager stopped completely." }
      }
    }
  }

  private val fetcherErrorHandler = CoroutineExceptionHandler { _, exception ->
    logger.error(exception) { "Fetcher failed. Stopping entire application." }
    scope.launch { stop() }
  }

  private fun workerErrorHandlerFor(index: Int, worker: SchedulerWorker) =
    CoroutineExceptionHandler { _, exception ->
      logger.error(exception) { "Worker $index failed. Attempting recovery." }

      if (isStopped.get()) return@CoroutineExceptionHandler
      if (failedWorkerCount.incrementAndGet() >= config.workers) {
        logger.error { "All workers have failed. Stopping application." }
        scope.launch { stop() }
        return@CoroutineExceptionHandler
      }

      scope.launch {
        worker.stop(index)
        runCatching {
          SchedulerWorkerFactory.build(config).start(index, fetcher.taskReadChannel)
          failedWorkerCount.set(0)
        }.onFailure { logger.error(it) { "Failed to restart worker $index" } }
      }
    }
}
