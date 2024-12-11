# Local Setup

This section describes how to set up and run the application locally for testing/debugging/developing purpose.

## Pre-Requisites

1. Java version 11 or higher (if running via gradle).
2. Docker installed on your machine.

## Setting up Providers

Before running the application, you need to set up the providers. The providers are the external services that Karya uses to store data, manage locks, and communicate between different components. The providers can be configured in the `providers.yml` file. A sample file can be found [here](../../configs/providers.yml).

### Providers in standalone mode

For local setup/testing/development, you can run the providers in standalone mode. The providers can be started using the following command:

```shell
docker-compose -f ./docs/local-setup/providers.docker-compose.yml up -d
```

This will instantiate the following services:
- Postgres as the repo provider (port `5432`)
- Redis as the locks provider (port `6379`)
- RabbitMQ as the queue provider (port `5672`)

Configure the values accordingly in your `providers.yml` file. Refer to the [Configuring adapters](./DATA_ADAPTERS.md#how-to-configure-adapters) section for more details.

### Providers in cluster mode

For testing Karya's ability to work in a distributed environment, you can run the providers in cluster mode. If one doesn't have any cluster setup, you can use the following command to start the providers in cluster mode:

<details>
<summary><strong>Postgres Cluster</strong></summary>

- This will setup a postgres cluster (1 Master, 1 Slave)
- Master runs on port `5432` and Slave runs on port `5433`

```shell
docker-compose -f ./docs/local-setup/cluster/postgres.docker-compose.yml up -d
```

</details>

<details>
<summary><strong>Redis Cluster</strong></summary>

- This will setup 3 redis instances.
- Replication has been purposely disabled for simplicity.
- All 3 run on different ports `7000`, `7001`, `7002`

1. Get the Local IP address of your machine

    For linux/Mac users:
    ```shell
   # get the local ip address
    ifconfig | grep "inet "
   
    # set it as environment variable
    export LOCAL_IP=192.168.0.111
    ```
   
    For Windows users:
    ```shell
   # get the local ip address
    ipconfig | findstr "IPv4"
   
    # set it as environment variable
    SET LOCAL_IP=192.168.0.111
   ```
2. Spin up the redis nodes:
    ```shell
    docker-compose -f ./docs/local-setup/cluster/redis.docker-compose.yml up -d
    ```
3. Create the redis cluster:
    ```shell
    zsh ./docs/local-setup/cluster/redis/create-cluster.sh
    ```

</details>

<details>
<summary><strong>RabbitMQ Cluster</strong></summary>

- This will setup a RabbitMQ cluster with 3 nodes.
- All 3 run on different ports `5672`, `5673`, `5674`

```shell
docker-compose -f ./docs/local-setup/cluster/rabbitmq.docker-compose.yml up -d
```

</details>

## Running the application

Now that the providers have been setup, it is time to run the application.

### Providing configuration files

1. Create a `providers.yml` file ([sample](../../configs/providers.yml)). Refer to the [data-adapters](./DATA_ADAPTERS.md) section for more information on how to configure the providers.
2. Create a `server.yml` file ([sample](../../configs/server.yml)). Refer to the [server](./COMPONENTS.md/#server) section for more information on how to configure the server.
3. Create a `scheduler.yml` file ([sample](../../configs/scheduler.yml)). Refer to the [scheduler](./COMPONENTS.md/#scheduler) section for more information on how to configure the scheduler.
4. Create a `executor.yml` file ([sample](../../configs/executor.yml)). Refer to the [executor](./COMPONENTS.md/#executor) section for more information on how to configure the executor.

### Setting environment variables

```shell
  export KARYA_PROVIDERS_CONFIG_PATH=path/to/providers.yml
  export KARYA_SERVER_CONFIG_PATH=path/to/server.yml
  export KARYA_SCHEDULER_CONFIG_PATH=path/to/scheduler.yml
  export KARYA_EXECUTOR_CONFIG_PATH=path/to/executor.yml
```

### Run the servers (via Gradle)

```shell
# start the server first
 ./gradlew servers-server:run
 
 # then start the executor
./gradlew servers-executor:run

# and finally start the scheduler
./gradlew servers-scheduler:run
```

Run a [MakePeriodicApiCall.kt.kt](../docs/samples/src/main/kotlin/karya/docs/samples/MakePeriodicApiCall.kt) to schedule a dummy plan and check if the setup is working fine.

### For testing Kafka Connector

- Start the Kafka service
- In the Kafka UI dashboard, create a cluster and provide the bootstrap server as `kafka:9090`
  ```shell
  docker-compose -f ./docs/local-setup/kafka.docker-compose.yml up -d
  ```