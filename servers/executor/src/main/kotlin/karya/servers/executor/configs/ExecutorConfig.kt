package karya.servers.executor.configs

import karya.connectors.chained.ChainedConnectorFactory
import karya.connectors.email.EmailConnectorFactory
import karya.connectors.kafka.KafkaConnectorFactory
import karya.connectors.restapi.RestApiConnectorFactory
import karya.connectors.slack.SlackConnectorFactory
import karya.core.actors.Connector
import karya.core.entities.Action
import karya.core.entities.Action.*
import karya.core.utils.getSection
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector
import karya.data.fused.di.components.FusedDataQueueComponent
import karya.data.fused.di.components.FusedDataRepoComponent
import karya.data.fused.di.factories.FusedDataQueueComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.executor.exceptions.ExecutorException
import kotlin.reflect.KClass
import karya.connectors.email.EmailConnectorConfig as Email
import karya.connectors.kafka.KafkaConnectorConfig as Kafka
import karya.connectors.restapi.RestApiConnectorConfig as RestApi
import karya.connectors.slack.SlackConnectorConfig as Slack


/**
 * Configuration class for the executor.
 *
 * @property metricsEnabled Whether metrics are enabled for the executor.
 * @property fusedDataRepoComponent The component for fused data repository.
 * @property fusedDataQueueComponent The component for fused data queue.
 * @property connectors A map of action types to their corresponding connectors.
 */
data class ExecutorConfig(
  val metricsEnabled: Boolean,
  val fusedDataRepoComponent: FusedDataRepoComponent,
  val fusedDataQueueComponent: FusedDataQueueComponent
) {

  lateinit var connectors: MutableMap<KClass<out Action>, Connector<out Action>>

  companion object {

    /**
     * Loads the executor configuration from the specified file paths.
     *
     * @param executorFilePath The path to the executor configuration file.
     * @param providerFilePath The path to the provider configuration file.
     * @return An instance of [ExecutorConfig] with the loaded configuration.
     */
    fun load(executorFilePath: String, providerFilePath: String): ExecutorConfig {

      val application: Map<String, *> = getSection(executorFilePath, "application")
      val repoConfig = RepoSelector.get(providerFilePath)
      val queueConfig = QueueSelector.get(providerFilePath)

      return ExecutorConfig(
        metricsEnabled = application["metricsEnabled"] as Boolean,
        fusedDataRepoComponent = FusedDataRepoComponentFactory.build(repoConfig),
        fusedDataQueueComponent = FusedDataQueueComponentFactory.build(queueConfig)
      )
        .addDefaultConnectors()
        .addCustomConnectors(application["connectors"] as List<*>)
    }

    /**
     * Adds the default connectors to the executor configuration.
     *
     * @return The updated [ExecutorConfig] with the default connectors added.
     */
    private fun ExecutorConfig.addDefaultConnectors(): ExecutorConfig {
      this.connectors = mutableMapOf()
      this.connectors[ChainedRequest::class] = ChainedConnectorFactory.build(this.fusedDataRepoComponent)
      return this
    }

    /**
     * Adds custom connectors to the executor configuration based on the provided properties.
     *
     * @param properties The list of properties defining the custom connectors.
     * @return The updated [ExecutorConfig] with the custom connectors added.
     * @throws ExecutorException.UnrecognizedConnectorPassedException If an unrecognized connector type is passed.
     */
    private fun ExecutorConfig.addCustomConnectors(properties: List<*>): ExecutorConfig {
      properties.forEach { connector ->
        val connectorMap = connector as? Map<*, *> ?: return@forEach
        val type = connectorMap["type"] as? String ?: return@forEach
        val configs = connectorMap["configs"] as? Map<*, *> ?: return@forEach

        when (type) {
          Kafka.IDENTIFIER -> this.connectors[KafkaProducerRequest::class] = KafkaConnectorFactory.build(configs)
          Email.IDENTIFIER -> this.connectors[EmailRequest::class] = EmailConnectorFactory.build(configs)
          RestApi.IDENTIFIER -> this.connectors[RestApiRequest::class] = RestApiConnectorFactory.build(configs)
          Slack.IDENTIFIER -> this.connectors[SlackMessageRequest::class] = SlackConnectorFactory.build(configs)

          else -> throw ExecutorException.UnrecognizedConnectorPassedException(type)
        }
      }
      return this
    }

  }
}
