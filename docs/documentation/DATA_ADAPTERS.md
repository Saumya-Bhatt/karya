# Data Adapters

The ability of Karya to _adapt_ to different data sources is what makes it a powerful tool. Karya provides various adapters so that users can integrate their existing data sources with Karya and start scheduling tasks.

Karya requires at least these 3 interfaces to be implemented for it to work:

1. **Repo** : A SQL based database for persistent storage
2. **Locks** : A distributed mutex lock to prevent race conditions
3. **Queue** : To buffers the execution of tasks.

> **NOTE** : Karya is built for high throughput and the tradeoff for it is precision as to when a task should be
> executed. But the hit in precision can be minimized by the configurability that Karya provides albeit at the cost of
> more resources.

---

## Supported Adapters

> All the adapters that Karya support can be used in a cluster mode as well making Karya even more fault tolerant.

Karya provides ability to just plug and play provided the implementation for the said interface is supported by it.
Users can easily connect their existing repo/locks/queues with Karya, spin up Karya's nodes and start scheduling tasks!
The following interfaces are currently implemented within Karya with rest more on the way:

| Repo Adapter                            | Locks Adapter              | Queue Adapter                         |
|-----------------------------------------|----------------------------|---------------------------------------|
| [Postgres](https://www.postgresql.org/) | [Redis](https://redis.io/) | [RabbitMQ](https://www.rabbitmq.com/) |
| | | [SQS](https://aws.amazon.com/sqs/)    |

---

## How to configure adapters?

You have your said components running. We must now provide a way for Karya to connect with it. We do this by specifying
all the configurations in a .yml file and set the environment variable `KARYA_PROVIDERS_CONFIG_PATH` to point to this
file.

Karya will read the contents of the yml file and connect to your repos/locks/queues! The format of the `providers.yml`
file is as follows:

```yml
repo:
  provider: "psql"
  partitions: 5  // how many partitions you would want in your database. Usefull when the throughput is high
  properties:
    // Properties of the repo provider

lock:
  provider: "redis"
  properties:
    // Properties of the locks provider

queue:
  provider: "rabbitmq"
  properties:
    // Properties of the queue provider
```

[This section](../../configs/README.md#providers) has sample `providers.yml` files that can be used to get started quickly.

Follow the below sections to configure exactly the data interface that you would want to attatch with Karya.

---

## Repo Adapters

This section describes the various repo interfaces that can be configured to work with Karya

### Configuring Postgres

> **providers.yml key:** *psql*

These are the properties that can/should be set for the Postgres repo interface:

| Key      | Description                                                                                                   |
|----------|---------------------------------------------------------------------------------------------------------------|
| *hikari* | One can set all the configurable options provided by hikari here just as you set them in a *.properties* file |

<details>
<summary><strong>Standalone mode example</strong></summary>

```yml
repo:
  provider: "psql"
  partitions: 5
  properties:
    hikari:
      dataSourceClassName: "org.postgresql.ds.PGSimpleDataSource"
      dataSource.user: "karya"
      dataSource.password: "karya"
      dataSource.databaseName: "karya"
      dataSource.portNumber: 5432
      dataSource.serverName: "localhost"
      maximumPoolSize: 10
      connectionTimeout: 5000
```

</details>

<details>
<summary><strong>Cluster mode example</strong></summary>

```yml
repo:
  provider: "psql"
  partitions: 5
  properties:
    hikari:
      dataSourceClassName: "org.postgresql.ds.PGSimpleDataSource"
      dataSource.user: "karya"
      dataSource.password: "karya"
      dataSource.databaseName: "karya"
      dataSource.url: "jdbc:postgresql://localhost:5432,localhost:5433/karya?loadBalanceStrategy=bestResponse&targetServerType=any&connectTimeout=1&failoverTimeout=30"
      maximumPoolSize: 10
      connectionTimeout: 5000
```

</details>

---

## Locks Adapters

This section describes the various locks interfaces that can be configured to work with Karya

### Configuring Redis

> **providers.yml key:** *redis*

These are the properties that can/should be set for the Redis locks interface:

| Key           | Description                                                                                                                    |
|---------------|--------------------------------------------------------------------------------------------------------------------------------|
| *clusterMode* | Set to true if you are using a Redis cluster                                                                                   |
| *password*    | The password to connect to the Redis cluster. Provide empty string if using in standalone mode (`clusterMode` is set to false) |
| *connection.timeout* | The timeout for the connection to the Redis server/cluster.                                                                    |
| *connection.poolSize* | The pool size for the connection to the Redis server/cluster.                                                                  |
| *connection.minimumIdleSize* | The minimum idle connections to the Redis server/cluster.                                                                      |
| *waitTime*    | Set the wait time for which redisson should wait before releasing a lock                                                       |
| *leaseTime*   | Set the lease time for which the lock should be held                                                                           |
| *clusterNodes* | The list of nodes in the Redis cluster. Provide the host and port of each node. When in standalone mode, the first node in the list will be used. |

<details>
<summary><strong>Standalone mode example</strong></summary>

```yml
lock:
  provider: "redis"
  properties:
    clusterMode: false
    password: karya
    waitTime: 1000
    leaseTime: 5000
    connection.timeout: 5000
    connection.poolSize: 5
    connection.minimumIdleSize: 2
    clusterNodes:
      - "redis://localhost:6379"
```

</details>

<details>
<summary><strong>Cluster mode example</strong></summary>

```yml
lock:
  provider: "redis"
  properties:
    clusterMode: true
    password: karya
    waitTime: 1000
    leaseTime: 5000
    connection.timeout: 5000
    connection.poolSize: 5
    connection.minimumIdleSize: 2
    clusterNodes:
      - "redis://localhost:7001"
      - "redis://localhost:7002"
      - "redis://localhost:7003"
```

</details>

---

## Queue Adapters

This section describes the various queue interfaces that can be configured to work with Karya

### Configuring RabbitMQ

> **providers.yml key:** *rabbitmq*

These are the properties that can/should be set for the RabbitMQ queue interface:

| Key            | Description                                                                 |
|----------------|-----------------------------------------------------------------------------|
| *username*     | The username to connect to the RabbitMQ server                               |
| *password*     | The password to connect to the RabbitMQ server                               |
| *virtualHost*  | The virtual host to connect to the RabbitMQ server                          |
| *clusterNodes* | The list of nodes in the RabbitMQ cluster. Provide the host and port of each node. When in standalone mode, the first node in the list will be used. |

<details>

<summary><strong>Standalone mode example</strong></summary>

```yml
queue:
  provider: "rabbitmq"
  properties:
    username: "karya"
    password: "karya"
    virtualHost: "/"
    clusterNodes:
      - "localhost:5672"
```

</details>

<details>

<summary><strong>Cluster mode example</strong></summary>

```yml
queue:
  provider: "rabbitmq"
  properties:
    username: "karya"
    password: "karya"
    virtualHost: "/"
    clusterNodes:
      - "localhost:5672"
      - "localhost:5673"
```

</details>

### Configuring SQS

> **providers.yml key:** *sqs*

These are the properties that can/should be set for the SQS queue interface:

| Key                     | Description                                                                                                                                           |
|-------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| *overrideLocalUrl*      | The local URL to override the SQS server URL. This is more useful when setting up locally using LocalStack. For production, set this to empty string. |
| *awsConfig.region*      | The region where the SQS server is hosted                                                                                                             |
| *awsConfig.accessKeyId* | The access key to connect to the SQS server                                                                                                           |
| *awsConfig.secretKey*   | The secret key to connect to the SQS server                                                                                                           |
| *queueConfig.executorQueueUrl*          | The URL of the Executor SQS queue to connect to                                                                                                       |
| *queueConfig.hooksQueueUrl*             | The URL of the Hooks SQS queue to connect to                                                                                                          |
| *queueConfig.deadLetterQueueUrl*        | The URL of the Dead Letter SQS queue to connect to                                                                                                    |
| *queueConfig.maxMessageFetched*         | The maximum number of messages to fetch at a time                                                                                                     |
| *queueConfig.longPollingWaitTime*       | The long polling wait time for the SQS queue                                                                                                          |
| *queueConfig.visibilityTimeout*         | The visibility timeout for the SQS queue                                                                                                              |

<details>

<summary><strong>Example</strong></summary>

```yml
queue:
  provider: "sqs"
  properties:
    overrideLocalUrl: "http://localhost:4566"
    awsConfig:
      region: "us-east-1"
      accessKeyId: " 1234"
      secretAccessKey: "1234"
    queueConfig:
      executorQueueUrl: "http://localhost:4566/000000000000/executor_queue.fifo"
      hooksQueueUrl: "http://localhost:4566/000000000000/hooks_queue.fifo"
      deadLetterQueueUrl: "http://localhost:4566/000000000000/dead_letter_queue.fifo"
      maxMessageFetched: 10
      longPollingWaitTime: 5
      visibilityTimeout: 60
```

</details>


