package karya.data.psql.di

import dagger.Binds
import dagger.Module
import karya.core.repos.*
import karya.data.psql.repos.*
import javax.inject.Singleton

@Module
abstract class PsqlModule {
  @Binds
  @Singleton
  abstract fun provideUsersRepo(psqlUsersRepo: PsqlUsersRepo): UsersRepo

  @Binds
  @Singleton
  abstract fun provideTasksRepo(psqlTasksRepo: PsqlTasksRepo): TasksRepo

  @Binds
  @Singleton
  abstract fun providePlansRepo(psqlPlansRepo: PsqlPlansRepo): PlansRepo

  @Binds
  @Singleton
  abstract fun provideErrorLogsRepo(psqlErrorLogsRepo: PsqlErrorLogsRepo): ErrorLogsRepo

  @Binds
  @Singleton
  abstract fun provideRepoConnector(psqlRepoConnector: PsqlRepoConnector): RepoConnector
}
