object Modules {
  const val CORE = ":core"
  const val CLIENT = ":client"
  const val CONNECTORS = ":connectors"

  object Data {
    private const val DATA = ":data"

    const val SQS = "$DATA-sqs"
    const val PSQL = "$DATA-psql"
    const val FUSED = "$DATA-fused"
    const val REDIS = "$DATA-redis"
    const val RABBIT_MQ = "$DATA-rabbitmq"
  }

  object Servers {
    private const val SERVERS = ":servers"

    const val SERVER = "$SERVERS-server"
    const val SCHEDULER = "$SERVERS-scheduler"
    const val EXECUTOR = "$SERVERS-executor"
  }
}
