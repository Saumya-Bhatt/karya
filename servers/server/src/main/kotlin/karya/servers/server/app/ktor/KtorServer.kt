package karya.servers.server.app.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import karya.servers.server.configs.ServerConfig
import java.time.Duration
import javax.inject.Inject

/**
 * Class responsible for managing the Ktor server.
 *
 * @property featureManager The manager for server features.
 * @property routeManager The manager for server routes.
 * @property config The configuration for the server.
 */
class KtorServer
@Inject
constructor(
  private val featureManager: FeatureManager,
  private val routeManager: RouteManager,
  private val config: ServerConfig
) {
  private val engine = buildEngine()

  companion object {
    private const val GRACE_PERIOD = 5L
    private const val TIMEOUT = 15L
  }

  /**
   * Starts the Ktor server.
   */
  fun start() {
    engine.start(false)
  }

  /**
   * Stops the Ktor server.
   */
  fun stop() {
    val gracePeriod = Duration.ofSeconds(GRACE_PERIOD).toMillis()
    val timeout = Duration.ofSeconds(TIMEOUT).toMillis()
    engine.stop(gracePeriod, timeout)
  }

  /**
   * Builds the embedded server engine.
   *
   * @return The embedded server engine.
   */
  private fun buildEngine() = embeddedServer(CIO, port = config.port, module = { main() })

  /**
   * Main module for the Ktor application.
   *
   * Wires the features and routes to the Ktor application.
   */
  private fun Application.main() {
    featureManager.apply { wireFeatures(config.meterRegistry) }
    routeManager.apply { wireRoutes(config.meterRegistry) }
  }
}
