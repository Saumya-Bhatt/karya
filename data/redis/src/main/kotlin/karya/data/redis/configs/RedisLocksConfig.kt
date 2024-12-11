package karya.data.redis.configs

import karya.core.configs.LocksConfig
import karya.core.utils.readValue

data class RedisLocksConfig(
  val waitTime: Long,
  val leaseTime: Long,
  val password: String,
  val clusterNodes: List<String>,
  val clusterMode: Boolean,
  val connectionTimeout: Int,
  val connectionPoolSize: Int,
  val connectionMinimumIdleSize: Int
) : LocksConfig(REDIS_IDENTIFIER) {

  companion object {
    const val REDIS_IDENTIFIER = "redis"
  }

  constructor(props: Map<*, *>) : this(
    clusterNodes = props.readValue("clusterNodes"),
    password = props.readValue("password"),
    clusterMode = props.readValue("clusterMode"),
    leaseTime = props.readValue("leaseTime"),
    waitTime = props.readValue("waitTime"),
    connectionTimeout = props.readValue("connection.timeout"),
    connectionPoolSize = props.readValue("connection.poolSize"),
    connectionMinimumIdleSize = props.readValue("connection.minimumIdleSize")
  )
}
