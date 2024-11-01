package karya.core.connectors

interface RepoConnector {
  suspend fun getPartitions(): Int
  suspend fun runMigration(): Boolean
  suspend fun createDynamicPartitions() : Boolean
  suspend fun shutdown() : Boolean
}