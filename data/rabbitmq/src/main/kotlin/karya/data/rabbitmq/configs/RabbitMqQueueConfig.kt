package karya.data.rabbitmq.configs

import karya.core.configs.QueueConfig
import karya.core.utils.readValue

data class RabbitMqQueueConfig(
  val username: String,
  val password: String,
  val virtualHost: String,
  val clusterNodes: List<String>
) : QueueConfig(RABBITMQ_IDENTIFIER) {

  companion object {
    const val RABBITMQ_IDENTIFIER = "rabbitmq"
  }

  constructor(props: Map<*, *>) : this(
    username = props.readValue("username"),
    password = props.readValue("password"),
    virtualHost = props.readValue("virtualHost"),
    clusterNodes = props.readValue("clusterNodes"),
  )

  fun provideAmqpUri(): String {
    val nodes = clusterNodes.joinToString(",")
    return "amqp://${username}:${password}@$nodes"
  }
}
