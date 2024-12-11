package karya.data.psql.di

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import karya.data.psql.configs.PsqlRepoConfig
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.inject.Singleton

@Module
class PsqlUtilsModule {
  @Provides
  @Singleton
  fun provideFlyway(dataSource: HikariDataSource): Flyway = Flyway
    .configure()
    .dataSource(dataSource).locations("classpath:db/migrations")
    .baselineOnMigrate(true)
    .placeholderReplacement(true)
    .load()

  @Provides
  @Singleton
  fun provideHikariDataSource(config: PsqlRepoConfig): HikariDataSource {
    val hikariConfig = HikariConfig(config.hikariProperties)
    return HikariDataSource(hikariConfig)
  }

  @Provides
  @Singleton
  fun provideDatabase(hikariDataSource: HikariDataSource): Database = Database.connect(hikariDataSource)
}
