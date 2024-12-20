package karya.servers.executor.app

import karya.core.configs.KaryaEnvironmentConfig
import karya.servers.executor.configs.ExecutorConfig
import karya.servers.executor.di.ExecutorServiceFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.concurrent.CountDownLatch

/**
 * The logger instance for logging messages.
 */
val logger: Logger = LogManager.getLogger("karaya.servers.executor.app.MainRunner")

/**
 * The main function to start the executor service.
 *
 * This function loads the configuration, builds the executor service, and starts it.
 * It also sets up a shutdown hook to stop the service gracefully.
 */
suspend fun main() {
  val providers = KaryaEnvironmentConfig.PROVIDERS
  val executor = KaryaEnvironmentConfig.EXECUTOR
  val config = ExecutorConfig.load(executor, providers)
  val service = ExecutorServiceFactory.build(config)

  val latch = CountDownLatch(1)

  Runtime.getRuntime().addShutdownHook(
    Thread {
      logger.info("Shutdown hook invoked...")
      service.stop()
      println("All executor instances stopped. Main thread exiting.")
    }
  )

  service.start()
  latch.await()
}
