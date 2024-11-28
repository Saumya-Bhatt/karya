package karya.servers.scheduler.di.factories

import karya.data.fused.di.factories.FusedDataLocksComponentFactory
import karya.data.fused.di.factories.FusedDataQueueComponentFactory
import karya.data.fused.di.factories.FusedDataRepoComponentFactory
import karya.servers.scheduler.configs.SchedulerConfig
import karya.servers.scheduler.di.components.DaggerSchedulerWorkerComponent

object SchedulerWorkerFactory {
  fun build(config: SchedulerConfig) =
    DaggerSchedulerWorkerComponent
      .builder()
      .fusedRepoComponent(FusedDataRepoComponentFactory.build(config.repoConfig))
      .fusedLocksComponent(FusedDataLocksComponentFactory.build(config.locksConfig))
      .fusedQueueComponent(FusedDataQueueComponentFactory.build(config.queueConfig))
      .executorConfig(config)
      .build()
      .schedulerWorker
}
