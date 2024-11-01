package karya.servers.scheduler.usecases

import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.usecases.external.GetOpenTask
import karya.servers.scheduler.usecases.external.ProcessTask
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.time.delay
import org.apache.logging.log4j.kotlin.Logging
import java.time.Duration
import javax.inject.Inject


class PollerService
@Inject
constructor(
  private val config: SchedulerConfig,
  private val getOpenTask: GetOpenTask,
  private val processTask: ProcessTask
) {

  companion object : Logging

  private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

  // Flow that emits a value at a fixed interval and calls the I/O function
  private val pollFlow = flow {
    while (true) {
      emit(getOpenTask.invoke())
      delay(Duration.ofMillis(config.pollFrequency))
    }
  }

  fun start() {
    scope.launch {
      pollFlow.catch { e ->
        println("[${config.getName()}] --- Error during polling: ${e.message}")
      }
        .collect { result ->
          if (result != null) {
            processTask.invoke(result)
          } else logger.info { "[${config.getName()}] --- Poller fetched 0 tasks..." }
        }
    }
  }

  fun stop() {
    scope.cancel()
  }
}