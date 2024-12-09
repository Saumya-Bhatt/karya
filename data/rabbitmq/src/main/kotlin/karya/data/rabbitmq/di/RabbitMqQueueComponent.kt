package karya.data.rabbitmq.di

import dagger.BindsInstance
import dagger.Component
import karya.core.queues.ConsumerQueueClient
import karya.core.queues.ProducerQueueClient
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    RabbitMqQueueModule::class,
    RabbitMqQueueUtilsModule::class,
  ],
)
interface RabbitMqQueueComponent {

  val producerQueueClient: ProducerQueueClient
  val consumerQueueClient: ConsumerQueueClient

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun rabbitMqConfig(rabbitMqQueueConfig: RabbitMqQueueConfig): Builder

    fun build(): RabbitMqQueueComponent
  }
}
