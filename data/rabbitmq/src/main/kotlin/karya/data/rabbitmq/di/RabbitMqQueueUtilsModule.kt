package karya.data.rabbitmq.di

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import dagger.Module
import dagger.Provides
import karya.data.rabbitmq.configs.RabbitMqQueueConfig
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
class RabbitMqQueueUtilsModule {

  @Provides
  @Singleton
  fun provideConnection(config: RabbitMqQueueConfig): Connection {
    val factory = ConnectionFactory()
    factory.username = config.username
    factory.password = config.password
    factory.virtualHost = config.virtualHost
    factory.setUri(config.provideAmqpUri())
    factory.isAutomaticRecoveryEnabled = true
    return factory.newConnection("karya-rabbitMq")
  }

  @Provides
  @Singleton
  fun provideChannel(connection: Connection): Channel = connection.createChannel()

  @Provides
  @Singleton
  fun provideJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = false
  }
}
