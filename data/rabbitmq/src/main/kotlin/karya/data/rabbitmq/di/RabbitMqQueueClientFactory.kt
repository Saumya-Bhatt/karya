package karya.data.rabbitmq.di

import karya.data.rabbitmq.configs.RabbitMqQueueConfig

object RabbitMqQueueClientFactory {

  fun buildProducer(config: RabbitMqQueueConfig) =
    DaggerRabbitMqQueueComponent
      .builder()
      .rabbitMqConfig(config)
      .build()
      .producerQueueClient

  fun buildConsumer(config: RabbitMqQueueConfig) =
    DaggerRabbitMqQueueComponent
      .builder()
      .rabbitMqConfig(config)
      .build()
      .consumerQueueClient
}
