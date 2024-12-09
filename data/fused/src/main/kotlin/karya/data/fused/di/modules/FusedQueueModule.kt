package karya.data.fused.di.modules

import dagger.Module
import dagger.Provides
import karya.core.configs.QueueConfig
import karya.data.fused.exceptions.FusedDataException.UnknownProviderException
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import karya.data.rabbitmq.di.RabbitMqQueueClientFactory
import javax.inject.Singleton

@Module
class FusedQueueModule {

  @Provides
  @Singleton
  fun provideProducerClient(queueConfig: QueueConfig) =
    when (queueConfig) {
      is RabbitMqQueueConfig -> RabbitMqQueueClientFactory.buildProducer(queueConfig)

      else -> throw UnknownProviderException("queue", queueConfig.provider)
    }

  @Provides
  @Singleton
  fun provideConsumerClient(queueConfig: QueueConfig) =
    when (queueConfig) {
      is RabbitMqQueueConfig -> RabbitMqQueueClientFactory.buildConsumer(queueConfig)

      else -> throw UnknownProviderException("queue", queueConfig.provider)
    }
}
