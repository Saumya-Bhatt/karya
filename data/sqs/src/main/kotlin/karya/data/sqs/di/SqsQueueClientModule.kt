package karya.data.sqs.di

import dagger.Binds
import dagger.Module
import karya.core.queues.ConsumerQueueClient
import karya.core.queues.ProducerQueueClient
import karya.data.sqs.SqsQueueClient
import javax.inject.Singleton

@Module
abstract class SqsQueueClientModule {

  @Binds
  @Singleton
  abstract fun provideSqsQueueProducerClient(client: SqsQueueClient): ProducerQueueClient

  @Binds
  @Singleton
  abstract fun provideSqsQueueConsumerClient(client: SqsQueueClient): ConsumerQueueClient
}
