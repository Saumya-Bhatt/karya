package karya.data.sqs.di

import karya.data.sqs.configs.SqsQueueConfig

object SqsQueueClientFactory {

  fun buildProducer(config: SqsQueueConfig) =
    DaggerSqsQueueClientComponent.builder()
      .sqsQueueConfig(config)
      .build()
      .producerQueueClient

  fun buildConsumer(config: SqsQueueConfig) =
    DaggerSqsQueueClientComponent.builder()
      .sqsQueueConfig(config)
      .build()
      .consumerQueueClient
}
