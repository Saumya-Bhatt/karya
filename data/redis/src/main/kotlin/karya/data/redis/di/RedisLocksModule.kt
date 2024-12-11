package karya.data.redis.di

import dagger.Module
import dagger.Provides
import karya.core.locks.LocksClient
import karya.data.redis.RedisLocksClient
import karya.data.redis.configs.RedisLocksConfig
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import javax.inject.Singleton

@Module
class RedisLocksModule {

  companion object {
    private const val CLIENT_NAME = "karya-redis"
  }

  @Provides
  @Singleton
  fun provideRedisLocksClient(
    config: RedisLocksConfig,
    redissonClient: RedissonClient,
  ): LocksClient = RedisLocksClient(redissonClient, config)

  @Provides
  @Singleton
  fun provideRedissonClient(redisLocksConfig: RedisLocksConfig): RedissonClient = when (redisLocksConfig.clusterMode) {
    true -> provideClusterRedissonClient(redisLocksConfig)
    false -> provideSingleRedissonClient(redisLocksConfig)
  }

  private fun provideSingleRedissonClient(redisLocksConfig: RedisLocksConfig): RedissonClient {
    val config = Config()
    config.useSingleServer()
      .setAddress(redisLocksConfig.clusterNodes.first())
      .setClientName(CLIENT_NAME)
      .setConnectionPoolSize(redisLocksConfig.connectionPoolSize)
      .setConnectionMinimumIdleSize(redisLocksConfig.connectionMinimumIdleSize)
      .setConnectTimeout(redisLocksConfig.connectionTimeout)
    return Redisson.create(config)
  }

  private fun provideClusterRedissonClient(redisLocksConfig: RedisLocksConfig): RedissonClient {
    val config = Config()
    config.useClusterServers()
      .addNodeAddress(*redisLocksConfig.clusterNodes.toTypedArray())
      .setClientName(CLIENT_NAME)
      .setConnectTimeout(redisLocksConfig.connectionTimeout)
      .setMasterConnectionPoolSize(redisLocksConfig.connectionPoolSize/2)
      .setMasterConnectionMinimumIdleSize(redisLocksConfig.connectionMinimumIdleSize/2)
      .setSlaveConnectionPoolSize(redisLocksConfig.connectionPoolSize/2)
      .setSlaveConnectionMinimumIdleSize(redisLocksConfig.connectionMinimumIdleSize/2)
      .password = redisLocksConfig.password
    return Redisson.create(config)
  }
}
