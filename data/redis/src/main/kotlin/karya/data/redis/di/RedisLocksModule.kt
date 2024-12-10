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
  @Provides
  @Singleton
  fun provideRedisLocksClient(
    config: RedisLocksConfig,
    redissonClient: RedissonClient,
  ): LocksClient = RedisLocksClient(redissonClient, config)

  @Provides
  @Singleton
  fun provideRedissonClient(redisLocksConfig: RedisLocksConfig): RedissonClient {
    val config = Config()
    config.useSingleServer().address = redisLocksConfig.host
    return Redisson.create(config)
  }
}
