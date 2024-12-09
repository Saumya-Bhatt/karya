package karya.data.rabbitmq.di

import dagger.Binds
import dagger.Module
import karya.core.queues.ConsumerQueueClient
import karya.core.queues.ProducerQueueClient
import karya.data.rabbitmq.RabbitMqQueueClient
import javax.inject.Singleton

@Module
abstract class RabbitMqQueueModule {

  @Binds
  @Singleton
  abstract fun provideRabbitMqQueueProducerClient(client: RabbitMqQueueClient): ProducerQueueClient

  @Binds
  @Singleton
  abstract fun provideRabbitMqQueueConsumerClient(client: RabbitMqQueueClient): ConsumerQueueClient
}
