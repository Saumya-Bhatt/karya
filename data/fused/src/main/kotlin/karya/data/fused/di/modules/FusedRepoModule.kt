package karya.data.fused.di.modules

import dagger.Module
import dagger.Provides
import karya.core.configs.RepoConfig
import karya.core.repos.*
import karya.data.fused.entities.ReposWrapper
import karya.data.fused.exceptions.FusedDataException.UnknownProviderException
import karya.data.psql.configs.PsqlRepoConfig
import karya.data.psql.di.PsqlComponentFactory
import javax.inject.Singleton

@Module
class FusedRepoModule {

  @Provides
  @Singleton
  fun provideRepoConnector(wrapper: ReposWrapper): RepoConnector = wrapper.repoConnector

  @Provides
  @Singleton
  fun provideUsersRepo(wrapper: ReposWrapper): UsersRepo = wrapper.usersRepo

  @Provides
  @Singleton
  fun providePlansRepo(wrapper: ReposWrapper): PlansRepo = wrapper.plansRepo

  @Provides
  @Singleton
  fun provideTasksRepo(wrapper: ReposWrapper): TasksRepo = wrapper.tasksRepo

  @Provides
  @Singleton
  fun provideErrorLogsRepo(wrapper: ReposWrapper): ErrorLogsRepo = wrapper.errorLogsRepo

  @Provides
  @Singleton
  fun provideReposWrapper(repoConfig: RepoConfig): ReposWrapper =
    when (repoConfig) {
      is PsqlRepoConfig -> providePsqlRepoWrapper(repoConfig)

      else -> throw UnknownProviderException("repo", repoConfig.provider)
    }

  private fun providePsqlRepoWrapper(psqlConfig: PsqlRepoConfig): ReposWrapper {
    val component = PsqlComponentFactory.build(psqlConfig)
    return ReposWrapper(
      plansRepo = component.plansRepo,
      usersRepo = component.usersRepo,
      tasksRepo = component.tasksRepo,
      errorLogsRepo = component.errorLogsRepo,
      repoConnector = component.repoConnector,
    )
  }
}
