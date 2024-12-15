# Components

Karya has the following components which helps it achieve its functionality. Note that **all of the components are
stateless** hence can be scaled in numbers according to your requirements without any issue.

---

## Client

Users can integrate the Karya client into their services to:

- Create a user
- Use that user to schedule tasks
- Update/cancel tasks

---

## Server

This is a web server via which the client interacts. It has the following functions:

1. Manage users. Only a registered user can schedule a task.
2. Pushes the task to the repo connector from which the scheduler will poll from

This is a configurable component as Karya will look for an environment variable `KARYA_SCERVER_CONFIG_PATH` to find the
.yml file that the scheduler instance will look for at runtime. The structure of the yml file is as follows:

| Server.yml Key             | Sample Value | Description                                                                                                                                                                                                          |
   |----------------------------|------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| *application.strictMode*   | true       | This key if set *false* will allow [chained plans](./CONNECTORS.md/#chained-plans) to be recurring in nature. Note that this can lead to number of tasks being scheduled exponentially, so **proceed with caution**. |
| *application.chainedDepth* | 2          | [Chained plans](./CONNECTORS.md/#chained-plans) are nothing but recursive triggers. As such, one can specify till what depth can this chain be, by setting this variable                                             |
| *application.port*         | 8080 | On which port you want the web-server to run                                                                                                                                                                         |
| *application.monitoringEnabled* | true | Whether you want to enable monitoring for the server. If set to true, the server will expose an endpoint `/metrics` which can be used to monitor the server. Refer to the [Observability](./OBSERVABILITY.md) section for more details on what metrics are exposed. |

[Sample server.yml file](../../configs/server.yml)

---

## Scheduler

- This is the heart of Karya. it will constantly keep polling the repo, and when it is time to execute the task, will it
  push to the queue.
- This is the component which has the logic as to when and if the next task is to be scheduled.

This is a configurable component as Karya will look for an environment variable `KARYA_SCHEDULER_CONFIG_PATH` to find
the .yml file that the scheduler instance will look for at runtime. The structure of the yml file is as follows:

| Scheduler.yml Key                     | Sample Value | Sescription                                                                                                                                                                                                                                |
 |---------------------------------------|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| *application.monitoringEabled*        | true         | Whether you want to enable monitoring for the scheduler. If set to true, the scheduler will expose business metrics at port `8081`. Refer to the [Observability](./OBSERVABILITY.md) section for more details on what metrics are exposed. |
| *application.threadCount*             | 2            | How many threads you want one instance of scheduler to use                                                                                                                                                                                 |
| *application.workers*                 | 3            | How many workers within the instance do you want to spin up. Useful when the throughput is high                                                                                                                                            |
| *application.fetcher.channelCapacity* | 10           | Executor *polls* for messages from the repo and pushes it to a local queue from which the workers consume from. This property defines the size of this queue explicitly to prevent a *OutOfMemory* scenario.                               |
| *application.fetcher.pollFrequency*   | 250          | Polling frequency from the repo, lesser the value, better the precision of when the task should be executed                                                                                                                                |
| *application.fetcher.executionBuffer* | 1000         | This specifies how far back from the time of polling should scheduler look for an unexecuted task. This should always be higher than pollFrequency                                                                                         |
| *application.fetcher.repoPartitions*  | List<Int>    | From which partitions of the repo should the scheduler instance should poll from.                                                                                                                                                          |

[Sample scheduler.yml file](../../configs/scheduler.yml)

---

## Executor

- Executors receive a task from the worker-queue and does the heavy work that the user specifies.
- User can specify what action should the executor perform once it receives the task. This is done via setting an
  environment variable `KARYA_EXECUTOR_CONFIG_PATH` that the executor will look for to initialize the connector-plugins
  at run time.
- [Connector-Plugins](./CONNECTORS.md) define what operations at the time of execution should the executor
  support.

| Executor.yml Key | Sample Value | Description                                                                                                                                                                                                                              |
|------------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| *application.monitoringEnabled* | true | Whether you want to enable monitoring for the executor. If set to true, the executor will expose business metrics at port `8082`. Refer to the [Observability](./OBSERVABILITY.md) section for more details on what metrics are exposed. |
| *application.connectors* | List<String> | List of connectors that the executor should support. Refer to the [Connector-Plugins](./CONNECTORS.md) section for more details on what connectors are supported. |

  [Sample executor.yml file](../../configs/executor.yml)

---
