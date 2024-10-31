object Modules {

  const val CORE = ":core"
  const val CLIENT = ":client"

  object Data {
    private const val DATA = ":data"

    const val PSQL = "$DATA-psql"
    const val FUSED = "$DATA-fused"
    const val REDIS = "$DATA-redis"
  }

  object Servers {
    private const val SERVERS = ":servers"

    const val SERVER = "$SERVERS-server"
    const val SCHEDULER = "$SERVERS-scheduler"
    const val WORKER = "$SERVERS-worker"
  }
}