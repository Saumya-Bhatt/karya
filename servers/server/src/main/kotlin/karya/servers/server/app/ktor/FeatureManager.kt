package karya.servers.server.app.ktor

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.CORS
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

  companion object {
    const val CORS_MAX_AGE = 3600L
  }

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

    // installing this to support karya web client
    install(CORS) {
      anyHost()
      allowMethod(HttpMethod.Options)
      allowMethod(HttpMethod.Get)
      allowMethod(HttpMethod.Post)
      allowMethod(HttpMethod.Patch)
      allowHeader(HttpHeaders.ContentType)
      allowHeader(HttpHeaders.Accept)
      maxAgeInSeconds = CORS_MAX_AGE
    }
  }
}
