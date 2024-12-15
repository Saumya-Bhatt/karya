package karya.servers.server.configs

import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import karya.core.configs.LocksConfig
import karya.core.configs.RepoConfig
import karya.core.utils.getSection
import karya.data.fused.LocksSelector
import karya.data.fused.RepoSelector

/**
 * Configuration class for the server.
 *
 * @property strictMode Indicates if strict mode is enabled.
 * @property maxChainedDepth The maximum depth of chained operations.
 * @property repoConfig The configuration for the repository.
 * @property locksConfig The configuration for the locks.
 * @property metricsEnabled Indicates if metrics are enabled.
 * @property port The port on which the server will run.
 */
data class ServerConfig(
  val strictMode: Boolean,
  val maxChainedDepth: Int,
  val repoConfig: RepoConfig,
  val locksConfig: LocksConfig,
  val metricsEnabled: Boolean,
  val port: Int
) {

  lateinit var meterRegistry: PrometheusMeterRegistry

  companion object {
    /**
     * Loads the server configuration from the specified file paths.
     *
     * @param providersFilePath The path to the providers file.
     * @param serverConfigFilePath The path to the server configuration file.
     * @return The loaded server configuration.
     */
    fun load(providersFilePath: String, serverConfigFilePath: String): ServerConfig {
      val section: Map<String, *> = getSection(serverConfigFilePath, "application")

      return ServerConfig(
        strictMode = section["strictMode"] as Boolean,
        maxChainedDepth = section["maxChainedDepth"] as Int,
        repoConfig = RepoSelector.get(providersFilePath),
        locksConfig = LocksSelector.get(providersFilePath),
        metricsEnabled = section["metricsEnabled"] as Boolean,
        port = section["port"] as Int
      ).apply { if (metricsEnabled) meterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT) }
    }
  }

}
