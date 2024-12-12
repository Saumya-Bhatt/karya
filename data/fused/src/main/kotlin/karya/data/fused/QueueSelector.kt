package karya.data.fused

import karya.core.configs.QueueConfig
import karya.core.utils.getSection
import karya.data.fused.exceptions.FusedDataException.UnknownProviderException
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import karya.data.rabbitmq.configs.RabbitMqQueueConfig.Companion.RABBITMQ_IDENTIFIER
import karya.data.sqs.configs.SqsQueueConfig
import karya.data.sqs.configs.SqsQueueConfig.Companion.SQS_IDENTIFIER

/**
 * Object responsible for selecting the appropriate queue configuration
 * based on the provided file path.
 */
object QueueSelector {

  /**
   * Retrieves the queue configuration from the specified file path.
   *
   * @param filePath The path to the configuration file.
   * @return The queue configuration.
   * @throws UnknownProviderException If the provider specified in the configuration is unknown.
   */
  fun get(filePath: String): QueueConfig {
    val section: Map<String, *> = getSection(filePath, "queue")
    val properties = section["properties"] as Map<*, *>

    return when (val provider = section["provider"]) {
      RABBITMQ_IDENTIFIER -> RabbitMqQueueConfig(properties)
      SQS_IDENTIFIER -> SqsQueueConfig(properties)

      else -> throw UnknownProviderException("queue", provider.toString())
    }
  }
}
