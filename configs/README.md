# Configs

This directory contains configuration files for the project. The configuration files are written in YAML format.

## Commons

This folder contains the configurations that are shared across the components. This would include:

- [log4j2.properties](./commons/log4j2.properties) - The logging configuration for the application.

## Migrations

This folder contains the configuration for the database migrations. For most part, Karya manages the database migrations. However, in some cases, manual migrations may be required.

In such case, the files present in the [migrations](./migrations) can be used to manually run the migration to set up the data layer.

## Providers

The `providers.yml` file contains the configuration for the data providers. The data providers are the sources of data for the application. The providers can be standalone or clustered. The configuration for the providers is explained in the [data-adapters](../docs/documentation/DATA_ADAPTERS.md) section.

### Standalone Providers Samples

The files are labeled according to : `<repo>-<locks>-<queue>.provider.yml`

- [psql-redis-rabbitmq.providers.yml](./providers/psql-redis-rabbitmq.providers.yml)
- [psql-redis-sqs.providers.yml](./providers/psql-redis-sqs.providers.yml)

### Clustered Providers Samples

The files are labeled according to : `<repo>-<locks>-<queue>.clustered.providers.yml`

- [psql-redis-rabbitmq.clustered.providers.yml](./providers/clustered/psql-redis-rabbitmq.clustered.providers.yml)

## Server

The `server.yml` file contains the configuration for the server. The server is the component that receives the data from the providers and processes it. The configuration for the server is explained in the [server](../docs/documentation/COMPONENTS.md#server) section.

[Sample server.yml](./server.yml)

## Scheduler

The `scheduler.yml` file contains the configuration for the scheduler. The scheduler is the component that schedules the tasks to be executed by the executor. The configuration for the scheduler is explained in the [scheduler](../docs/documentation/COMPONENTS.md#scheduler) section.

[Sample scheduler.yml](./scheduler.yml)

## Executor

The `executor.yml` file contains the configuration for the executor. The executor is the component that executes the tasks scheduled by the scheduler. The configuration for the executor is explained in the [executor](../docs/documentation/COMPONENTS.md#executor) section.

[Sample executor.yml](./executor.yml)
