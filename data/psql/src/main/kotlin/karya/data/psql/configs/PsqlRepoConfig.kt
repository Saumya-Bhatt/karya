package karya.data.psql.configs

import karya.core.configs.RepoConfig
import karya.core.utils.extractProperties
import java.util.*

data class PsqlRepoConfig(
  val hikariProperties: Properties,
  override val partitions: Int,
) : RepoConfig(PSQL_IDENTIFIER, partitions) {

  companion object {
    const val PSQL_IDENTIFIER = "psql"
  }

  constructor(props: Map<*, *>, partitions: Int) : this(
    hikariProperties = extractProperties(props, "hikari"),
    partitions = partitions,
  )
}
