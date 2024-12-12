package karya.data.sqs.di

import dagger.BindsInstance
import dagger.Component
import karya.core.queues.ConsumerQueueClient
import karya.core.queues.ProducerQueueClient
import karya.data.sqs.configs.SqsQueueConfig
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    SqsQueueClientModule::class,
    SqsQueueClientUtilsModule::class,
  ],
)
interface SqsQueueClientComponent {

  val producerQueueClient: ProducerQueueClient
  val consumerQueueClient: ConsumerQueueClient

  @Component.Builder
  interface Build {

    @BindsInstance
    fun sqsQueueConfig(sqsQueueConfig: SqsQueueConfig): Build

    fun build(): SqsQueueClientComponent
  }
}
