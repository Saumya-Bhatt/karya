package karya.servers.scheduler.usecases

import io.prometheus.client.Summary
import io.prometheus.client.exporter.HTTPServer
import karya.servers.scheduler.app.logger

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
  private const val METRICS_EXPOSED_PORT = 8081

  private lateinit var prometheusServer: HTTPServer

  /**
   * Summary of time from when the task was meant to be executed to when it was fetched by the scheduler
   * and pushed to the internal queue.
   */
  val taskFetchLatencySummary: Summary = Summary.build()
    .name("karya_scheduler_task_fetch_latency_seconds_summary")
    .help(
      """
      Summary of time from when the task was meant to be executed to when it was fetched by the scheduler
      and pushed to the internal queue.
      This is useful to accordingly adjust the polling frequency.
    """.trimIndent()
    )
    .quantile(P50, ALLOWED_TOLERANCE)
    .quantile(P75, ALLOWED_TOLERANCE)
    .quantile(P90, ALLOWED_TOLERANCE)
    .quantile(P95, ALLOWED_TOLERANCE)
    .quantile(P99, ALLOWED_TOLERANCE)
    .register()

  /**
   * Summary of time from when the task was fetched from the internal queue to when it was processed by the worker
   * and pushed to the external queue.
   */
  val taskProcessLatencySummary: Summary = Summary.build()
    .name("karya_scheduler_task_process_latency_seconds_summary")
    .help(
      """
      Summary of time from when the task was fetched from the internal queue to when it was processed by the worker
      and pushed to the external queue.
      This is useful to accordingly adjust the number of scheduler workers.
    """.trimIndent()
    )
    .quantile(P50, ALLOWED_TOLERANCE)
    .quantile(P75, ALLOWED_TOLERANCE)
    .quantile(P90, ALLOWED_TOLERANCE)
    .quantile(P95, ALLOWED_TOLERANCE)
    .quantile(P99, ALLOWED_TOLERANCE)
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
