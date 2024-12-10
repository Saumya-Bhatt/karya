package karya.data.redis.configs

import karya.core.configs.LocksConfig
import karya.core.utils.readValue

data class RedisLocksConfig(
  val host: String,
  val waitTime: Long,
  val leaseTime: Long,
) : LocksConfig(REDIS_IDENTIFIER) {

  companion object {
    const val REDIS_IDENTIFIER = "redis"
  }

  constructor(props: Map<*, *>) : this(
    host = props.readValue("host"),
    leaseTime = props.readValue("leaseTime"),
    waitTime = props.readValue("waitTime"),
  )
}
