# Connectors

This here is the section which defines what all actions do Karya supports when a task has to be executed by the
executor.
This has to be set in the **executor.yml** file before starting the executor. Currently, the following connector plugins are
available:

| Executor.yml key | Description                                                 |
|-----------|-------------------------------------------------------------|
| *restapi* | This plugin helps support the executor make a REST Api call |
| *slack*   | This plugin helps support the executor send a slack message |
| *email*   | This plugin helps support the executor send an email        |
| *kafka*   | This plugin helps support the executor publish a message to a Kafka topic |


### Configuring Rest API

> **executor.yml type key:** *restapi*

| Key        | Description                                                                                                                      |
|------------|----------------------------------------------------------------------------------------------------------------------------------|
| *keepAliveTime* | For how long does the connection needs to be kept alive for while waiting for a response.                                        |
| *connectionTimeout*  | Timeout limit set on the executor while making the API call                                                                      |
| *connectionAttempts*   | How many network connection attempts must be connector make before declaring downstream as non-responsive.                       |

<details>

<summary><strong>Example</strong></summary>

```yml
    - type: "restapi"
      configs:
        keepAliveTime: 3000
        connectionTimeout: 1000
        connectionAttempts: 3
```

</details>

Refer to the [Action.RestApiRequest](../../core/src/main/kotlin/karya/core/entities/Action.kt) object for more details on the parameters that can be set while creating a plan.

[Sample MakePeriodicRestApiCall.kt example](../../docs/samples/src/main/kotlin/karya/docs/samples/MakePeriodicApiCall.kt).

### Configuring Slack

> **executor.yml type key:** *slack*

| Key        | Description                                                                                                                      |
|------------|----------------------------------------------------------------------------------------------------------------------------------|
| *token*    | The token to be used to authenticate the slack message.                                                                          |

<details>

<summary><strong>Example</strong></summary>

```yml
- type: "slack"
  configs:
    token: "xoxb"
```

</details>

Refer to the [Action.SlackMessage](../../core/src/main/kotlin/karya/core/entities/Action.kt) object for more details on the parameters that can be set while creating a plan.

[Sample MakePeriodicSlackCallWithFailureHook.kt example](../../docs/samples/src/main/kotlin/karya/docs/samples/MakePeriodicSlackCallWithFailureHook.kt).

### Configuring Email

> **executor.yml type key:** *email*

| Key        | Description                                       |
|------------|---------------------------------------------------|
| *username* | The username to be used to send the email.        |
| *password* | The password to be used to send the email.        |
| *smtp*     | The smtp properties to be used to send the email. |

<details>

<summary><strong>Example</strong></summary>

```yml
    - type: "email"
      configs:
        username: "your-email@gmail.com"
        password: "your-app-password"
        smtp:
          mail.smtp.auth: "true"
          mail.smtp.starttls.enable: "true"
          mail.smtp.host: "smtp.gmail.com"
          mail.smtp.port: "587"
```

</details>


Refer to the [Action.EmailRequest](../../core/src/main/kotlin/karya/core/entities/Action.kt) object for more details on the parameters that can be set while creating a plan.

[Sample MakeDelayEmailRequest.kt example](../../docs/samples/src/main/kotlin/karya/docs/samples/MakeDelayEmailRequest.kt).

### Configuring Kafka

> **executor.yml type key:** *kafka*

**NOTE:** Message pushed will be a JSON string. The message will be pushed to the topic specified in the plan.

| Key                 | Description                                                                                                                      |
|---------------------|----------------------------------------------------------------------------------------------------------------------------------|
| *bootstrap.servers* | The Kafka bootstrap servers to be used to publish the message.                                                                  |

<details>

<summary><strong>Example</strong></summary>

```yml
    - type: "kafka"
      configs:
        bootstrap.servers: "localhost:9092"
```

</details>

Refer to the [Action.KafkaProducerRequest](../../core/src/main/kotlin/karya/core/entities/Action.kt) object for more details on the parameters that can be set while creating a plan.

[Sample MakePeriodicPublishingToKafka.kt example](../../docs/samples/src/main/kotlin/karya/docs/samples/MakePeriodicPublishingToKafka.kt).

---

## Default Connector Plugins

Some plugins need not be specified in the `executor.yml` file as they are natively supported by the Executor. They are:

### Chained Plans

At times, one would have a use case where they would want to schedule a periodic task, *starting* from a definite point
in the future. This could be achieved by scheduling a one time task to trigger at the that point in time in the future.
And that trigger would be to schedule the periodic task that you want to run. We call this **chained plans**.

> Karya will monitor the lifecycle of chained plans, i.e. on cancelling the parent plan, Karya will automatically cancel all the child/grandchild plans that got triggered!

- Karya supports chained plans, albeit with some configuration which needs to be specified in the [server.yml](../documentation/COMPONENTS.md/#server).

- Refer to the [Action.ChainedRequest](../../core/src/main/kotlin/karya/core/entities/Action.kt) object for more details on the parameters that can be set while creating a plan.

[Sample MakeChainedKaryaCall.kt.kt example](./docs/samples/src/main/kotlin/karya/docs/samples/MakeChainedKaryaCall.kt.kt).
