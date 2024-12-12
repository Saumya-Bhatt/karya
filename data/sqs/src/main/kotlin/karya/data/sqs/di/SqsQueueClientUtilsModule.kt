package karya.data.sqs.di

import dagger.Module
import dagger.Provides
import karya.data.sqs.configs.AwsConfig
import karya.data.sqs.configs.QueueConfig
import karya.data.sqs.configs.SqsQueueConfig
import kotlinx.serialization.json.Json
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI
import javax.inject.Singleton

@Module
class SqsQueueClientUtilsModule {

  @Provides
  @Singleton
  fun provideSqsClient(config: SqsQueueConfig): SqsClient {
    val builder = SqsClient.builder()
      .region(Region.of(config.awsConfig.region))
      .credentialsProvider(provideStaticCredentialsProvider(config.awsConfig))
    if(config.overrideLocalUrl.isNullOrEmpty()) return builder.build()
    return builder.endpointOverride(URI.create(config.overrideLocalUrl)).build()
  }

  @Provides
  @Singleton
  fun provideQueueConfig(config: SqsQueueConfig): QueueConfig =
    config.queueConfig

  @Provides
  @Singleton
  fun provideJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = false
  }

  private fun provideStaticCredentialsProvider(config: AwsConfig): StaticCredentialsProvider =
    StaticCredentialsProvider.create(
      AwsBasicCredentials.create(config.accessKeyId, config.secretAccessKey)
    )

}
