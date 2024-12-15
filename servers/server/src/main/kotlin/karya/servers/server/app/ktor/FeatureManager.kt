package karya.servers.server.app.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import javax.inject.Inject

/**
 * Class responsible for managing the features of the Ktor server.
 */
class FeatureManager
@Inject
constructor() {

  /**
   * Wires the features to the Ktor application.
   *
   * Installs the ContentNegotiation and CallLogging plugins.
   *
   * @receiver The Ktor application instance.
   */
  @OptIn(ExperimentalSerializationApi::class)
  fun Application.wireFeatures(meterRegistry: PrometheusMeterRegistry) {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
          allowStructuredMapKeys = true
          namingStrategy = JsonNamingStrategy.SnakeCase
          useAlternativeNames = true
          encodeDefaults = true
        },
      )
    }

    install(CallLogging) {
      format { call ->
        val status = call.response.status()
        val httpMethod = call.request.httpMethod.value
        val endpoint = call.request.uri
        "$status - $httpMethod $endpoint"
      }
    }

    install(MicrometerMetrics) {
      registry = meterRegistry
      meterBinders = listOf(
        JvmMemoryMetrics(),
        JvmGcMetrics(),
        ProcessorMetrics()
      )
    }
  }
}
