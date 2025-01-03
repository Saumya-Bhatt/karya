# Local Setup

This section describes how to set up and run the application locally for testing/debugging/developing purpose.

---

## Pre-Requisites

1. Java version 11 or higher (if running via gradle).
2. Docker installed on your machine.

---

## Setting up Providers

Before running the application, you need to set up the providers. The providers are the external services that Karya uses to store data, manage locks, and communicate between different components. The providers can be configured in the `providers.yml` file.

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

- This will set up a postgres cluster (1 Master, 1 Slave)
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

1. Get the Local IP address of your machine and set it as environment variable.

    ```shell
   # For linux/mac users
    ifconfig | grep "inet "
   
   # For windows users:
   ipconfig | findstr "IPv4"
   ```
   
   ```
    # set it as environment variable
    export LOCAL_IP=192.168.0.111
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

---

## Running the application

Now that the providers have been set up, it is time to run the application.

### Providing configuration files

[Config Section](../../configs/README.md#providers) contains the sample configuration files for different providers.

1. Create a `providers.yml`. Refer to the [data-adapters](./DATA_ADAPTERS.md) section for more information on how to configure the providers. Sample files can be found [here](../../configs/README.md#providers).
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

### Run the servers (via Docker-Compose) [Recommended]

The `docker-compose` file provided in the `docs/local-setup` directory can be used to run the servers. The `docker-compose` file will start the server, executor, and scheduler containers. Do note the following:

- Each container will have a memory limit of `512m` for `Xms` and `1536m` for `Xmx`.
- The compose file will spin up 1 node of each server.
- The compose file will use these [providers.yml](../../configs/providers/psql-redis-rabbitmq.providers.yml), [server.yml](../../configs/server.yml), [scheduler.yml](../../configs/scheduler.yml), and [executor.yml](../../configs/executor.yml) files

```shell
docker-compose -f ./docs/local-setup/karya.docker-compose.yml up -d
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

### Run the servers (via Docker)

Each server can be run as individual docker containers. One just needs to configure the memory requirement of each container and provide the path to the configuration files.

> Default memory settings are `512m` for `Xms` and `1536m` for `Xmx`.

#### Running Karya Server

[DockerHub Image](https://hub.docker.com/r/saumyabhatt10642/karya-server) / [GitHub Container Registry](https://github.com/Saumya-Bhatt/karya/pkgs/container/karya-server)

```shell
docker run -d \
  --name karya-server \
  -e MEMORY_XMS=512m \
  -e MEMORY_XMX=1536m \
  -v /Users/saumya.bhatt/Desktop/Saumya/karya/configs/providers/psql-redis-rabbitmq.providers.yml:/home/app/configs/psql-redis-rabbitmq.providers.yml \
  -v /Users/saumya.bhatt/Desktop/Saumya/karya/configs/server.yml:/home/app/configs/server.yml \
  -e KARYA_PROVIDERS_CONFIG_PATH=/home/app/configs/psql-redis-rabbitmq.providers.yml \
  -e KARYA_SERVER_CONFIG_PATH=/home/app/configs/server.yml \
  saumyabhatt10642/karya-server
````

#### Running Karya Executor

[DockerHub Image](https://hub.docker.com/r/saumyabhatt10642/karya-executor) / [GitHub Container Registry](https://github.com/Saumya-Bhatt/karya/pkgs/container/karya-executor)

```shell
docker run -d \
  --name karya-executor \
  -e MEMORY_XMS=512m \
  -e MEMORY_XMX=1536m \
  -v /Users/saumya.bhatt/Desktop/Saumya/karya/configs/providers/psql-redis-rabbitmq.providers.yml:/home/app/configs/psql-redis-rabbitmq.providers.yml \
  -v /Users/saumya.bhatt/Desktop/Saumya/karya/configs/executor.yml:/home/app/configs/executor.yml \
  -e KARYA_PROVIDERS_CONFIG_PATH=/home/app/configs/psql-redis-rabbitmq.providers.yml \
  -e KARYA_EXECUTOR_CONFIG_PATH=/home/app/configs/executor.yml \
  saumyabhatt10642/karya-executor
```

### Running Karya Scheduler

[DockerHub Image](https://hub.docker.com/r/saumyabhatt10642/karya-scheduler) / [GitHub Container Registry](https://github.com/Saumya-Bhatt/karya/pkgs/container/karya-scheduler)

```shell
docker run -d \
  --name karya-scheduler \
  -e MEMORY_XMS=512m \
  -e MEMORY_XMX=1536m \
  -v /Users/saumya.bhatt/Desktop/Saumya/karya/configs/providers/psql-redis-rabbitmq.providers.yml:/home/app/configs/psql-redis-rabbitmq.providers.yml \
  -v /Users/saumya.bhatt/Desktop/Saumya/karya/configs/scheduler.yml:/home/app/configs/scheduler.yml \
  -e KARYA_PROVIDERS_CONFIG_PATH=/home/app/configs/psql-redis-rabbitmq.providers.yml \
  -e KARYA_SCHEDULER_CONFIG_PATH=/home/app/configs/scheduler.yml \
  saumyabhatt10642/karya-scheduler
```

Run a [MakePeriodicApiCall.kt.kt](../samples/src/main/kotlin/karya/docs/samples/MakePeriodicApiCall.kt) to schedule a dummy plan and check if the setup is working fine. More samples can be found [here](../samples/src/main/kotlin/karya/docs/samples).

---

## Karya Server API docs

The Karya server exposes a set of REST APIs that can be used to interact with the server. The Postman collection for the same can be found [here](../media/Karya.postman_collection.json).

---

## Testing with external services

Sample docker-compose files are provided in the `docs/local-setup/test-helpers` directory to test the application with external services like Kafka, SQS, etc.

### For testing Kafka Connector

- Start the Kafka service
- In the Kafka UI dashboard, create a cluster and provide the bootstrap server as `kafka:9090`
  ```shell
  docker-compose -f ./docs/local-setup/test-helpers/kafka.docker-compose.yml up -d
  ```

### For testing locally with AWS SQS as a queue provider

- Start the SQS service and create the necessary queues.
    ```shell
    docker-compose -f ./docs/local-setup/test-helpers/sqs.docker-compose.yml up -d
    ```
- Fetch the queue URL from the `create-queues` container logs and update the `providers.yml` file accordingly.

### For testing observability/creating new metrics

Refer to the [Observability](./OBSERVABILITY.md) section for more details on how to create new metrics.

- Start the Prometheus and Grafana services.
    ```shell
    docker-compose -f ./docs/local-setup/test-helpers/observability.docker-compose.yml up -d
    ```
- Access the Grafana dashboard at `http://localhost:3000` with credentials `admin/admin`.
- Add the Prometheus data source with URL `http://prometheus:9090`.
- Import the dashboard from [Karya Monitoring Dashboard v1.json](../local-setup/observability/Karya_Monitoring_Dashboard_v1.json).
