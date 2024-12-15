package karya.servers.scheduler.configs

import karya.core.configs.LocksConfig
import karya.core.configs.QueueConfig
import karya.core.configs.RepoConfig
import karya.core.exceptions.ConfigException.YamlMapKeyNotSetException
import karya.core.utils.getSection
import karya.core.utils.readValue
import karya.data.fused.LocksSelector
import karya.data.fused.QueueSelector
import karya.data.fused.RepoSelector

/**
 * Configuration class for the scheduler.
 *
 * @property threadCount The number of threads to be used by the scheduler.
 * @property workers The number of worker instances.
 * @property channelCapacity The capacity of the task channel.
 * @property pollFrequency The frequency at which tasks are polled.
 * @property partitions The list of partitions from which to poll from.
 * @property executionBufferInMilli The buffer time in milliseconds while fetching a task to be executed.
 * @property metricsEnabled Whether metrics are enabled.
 * @property repoConfig The configuration for the repository.
 * @property locksConfig The configuration for the locks.
 * @property queueConfig The configuration for the queue.
 */
data class SchedulerConfig(
  val threadCount: Int,
  val workers: Int,
  val channelCapacity: Int,
  val pollFrequency: Long,
  val partitions: List<Int>,
  val executionBufferInMilli: Long,
  val metricsEnabled: Boolean,
  val repoConfig: RepoConfig,
  val locksConfig: LocksConfig,
  val queueConfig: QueueConfig,
) {

  companion object {
    /**
     * Loads the scheduler configuration from the specified file paths.
     *
     * @param schedulerFilePath The path to the scheduler configuration file.
     * @param providerFilePath The path to the provider configuration file.
     * @return The loaded [SchedulerConfig] instance.
     * @throws YamlMapKeyNotSetException If a required key is not set in the YAML file.
     */
    fun load(
      schedulerFilePath: String,
      providerFilePath: String,
    ): SchedulerConfig {

      val application: Map<String, *> = getSection(schedulerFilePath, "application")
      val fetcher = application["fetcher"] as Map<*, *>?
        ?: throw YamlMapKeyNotSetException("application.fetcher")

      return SchedulerConfig(
        metricsEnabled = application.readValue("metricsEnabled"),
        threadCount = application.readValue("threadCount"),
        workers = application.readValue("workers"),

        channelCapacity = fetcher.readValue("channelCapacity"),
        pollFrequency = fetcher.readValue("pollFrequency"),
        executionBufferInMilli = fetcher.readValue("executionBuffer"),
        partitions = getPartitions(fetcher),

        repoConfig = RepoSelector.get(providerFilePath),
        locksConfig = LocksSelector.get(providerFilePath),
        queueConfig = QueueSelector.get(providerFilePath),
      )
    }

    /**
     * Retrieves the list of partitions from the fetcher properties.
     *
     * @param fetcherProperties The properties of the fetcher.
     * @return The list of partitions.
     * @throws IllegalArgumentException If the `repoPartitions` key is not set.
     */
    private fun getPartitions(fetcherProperties: Map<*, *>) =
      fetcherProperties["repoPartitions"]?.let { it as List<*> }?.map { it as Int }
        ?: throw IllegalArgumentException("repoPartitions is required")
  }
}
