package karya.servers.executor.usecase

import io.prometheus.client.Counter
import io.prometheus.client.Summary
import io.prometheus.client.exporter.HTTPServer
import karya.servers.executor.app.logger

/**
 * Object responsible for managing Prometheus metrics.
 */
object MetricsManager {

  private const val P50 = 0.5
  private const val P75 = 0.75
  private const val P90 = 0.9
  private const val P95 = 0.95
  private const val P99 = 0.99
  private const val ALLOWED_TOLERANCE = 0.01
  private const val METRICS_EXPOSED_PORT = 8082

  private lateinit var prometheusServer: HTTPServer

  /**
   * Summary of time from when the task was meant to be executed to when it was fetched by the executor
   */
  val taskPolledLatencySummary: Summary = Summary.build()
    .name("karya_executor_task_polled_latency_summary")
    .help(
      """
      Summary of time from when the task was meant to be executed to when it was fetched by the executor.
      This is useful to accordingly adjust the polling frequency and set the number of executors.
      """.trimIndent()
    )
    .quantile(P50, ALLOWED_TOLERANCE)
    .quantile(P75, ALLOWED_TOLERANCE)
    .quantile(P90, ALLOWED_TOLERANCE)
    .quantile(P95, ALLOWED_TOLERANCE)
    .quantile(P99, ALLOWED_TOLERANCE)
    .register()

  /**
   * Summary of time from when the executor started executing the task to when it was marked as complete
   */
  val taskExecutionLatencySummary: Summary = Summary.build()
    .name("karya_executor_task_execution_latency_summary")
    .help(
      """
      Summary of time from when the executor started executing the task to when it was marked as complete.
      Note that Karya will only consider a task to be executed "on-time" once the execution starts. And not when
      it has been marked as completed.
      """.trimIndent()
    )
    .quantile(P50, ALLOWED_TOLERANCE)
    .quantile(P75, ALLOWED_TOLERANCE)
    .quantile(P90, ALLOWED_TOLERANCE)
    .quantile(P95, ALLOWED_TOLERANCE)
    .quantile(P99, ALLOWED_TOLERANCE)
    .register()

  /**
   * Counter for the number of tasks that were successfully executed
   */
  val taskSuccessExecutionCount: Counter = Counter.build()
    .name("karya_executor_task_success_execution_count")
    .help("Number of tasks that were successfully executed.")
    .register()

  /**
   * Counter for the number of tasks that failed to execute
   */
  val taskFailedExecutionCount: Counter = Counter.build()
    .name("karya_executor_task_failed_execution_count")
    .help("Number of tasks that failed to execute.")
    .register()

  /**
   * Starts the Prometheus metrics server.
   */
  fun startMetrics() {
    prometheusServer = HTTPServer(METRICS_EXPOSED_PORT)
    logger.info("Prometheus metrics server started at port $METRICS_EXPOSED_PORT")
  }

  /**
   * Stops the Prometheus metrics server.
   */
  fun stopMetrics() {
    prometheusServer.stop()
    logger.info("Prometheus metrics server stopped.")
  }

}
