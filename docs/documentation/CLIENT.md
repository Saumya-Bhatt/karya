# Java Client

This section helps describe what all various plans one can scheduler using Karya Java Client.

> ### Python Client
> 
> One can refer to the [usage section](https://github.com/Saumya-Bhatt/karya-python-client?tab=readme-ov-file#useage-examples) here if using the python client to interact with Karya.

[Sample Kotlin Examples](../samples/src/main/kotlin/karya/docs/samples/)

---

## Initial Setup

### Installing Karya Client

Karya client can be found at the [Maven Central](https://central.sonatype.com/artifact/io.github.saumya-bhatt/karya-client) or from [GitHub Packages](https://github.com/Saumya-Bhatt/karya/packages/2353009)

```kotlin
// add this to your build.gradle.kts
dependencies {
  implementation("io.github.saumya-bhatt:karya-client:<latest-version>")
  implementation("io.github.saumya-bhatt:karya-core:<latest-version>")
}
```

> **NOTE:** Minimum Java version required is **11** 

#### 1. Creating the client

First create the config object:

```kotlin
import karya.client.configs.KaryaClientConfig

// this is a companion object to connect to Karya servers running locally
val config = KaryaClientConfig.Dev
```

Then create the client object:
```kotlin
import karya.client.di.KaryaClientFactory

val client = KaryaClientFactory.create(config)
```

#### 2. Creating a user

Only a user registered with Karya can schedule tasks. To create a user, use the following code:

```kotlin
import karya.core.entities.requests.CreateUserRequest

val user = client.createUser(CreateUserRequest("Bob"))
```

#### 3. Creating an action

Specify the action that you would want to trigger once the task is scheduled.

```kotlin
import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.http.Body
import karya.core.entities.http.Method
import karya.core.entities.http.Protocol

// Define a REST API request action
val apiRequest = Action.RestApiRequest(
  protocol = Protocol.HTTPS,
  baseUrl = "google.com",
  method = Method.POST,
  headers = mapOf(
    "content-type" to "application/json",
    "client-header" to "Alice",
  ),
  body = Body.JsonBody(
    data = mapOf(
      "udf1" to "value",
      "udf2" to 1,
      "udf3" to true,
      "udf4" to 1.2,
      "udf5" to listOf(1, 2, 3, 4),
      "udf6" to mapOf(
        "nested-udf1" to listOf("a", "b", "c"),
        "nested-udf2" to mapOf("nested-nested-udf1" to true),
      ),
      "udf7" to listOf(
        mapOf("nested-udf3" to listOf(1, 2, 3)),
      ),
    ),
  ),
  timeout = 1000L,
)
```

#### 4. Submit the plan to Karya.

> **NOTE:** period_time has to be in the [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601#Durations) format.

```kotlin
import karya.core.entities.requests.SubmitPlanRequest


// Create a plan request with the defined API request action
val planRequest = SubmitPlanRequest(
  userId = user.id,
  description = "Sample run",
  periodTime = "PT7S",
  planType = PlanType.Recurring(Instant.now().plusSeconds(30).toEpochMilli()),
  action = apiRequest
)

// Submit the plan request and print the result
val plan = client.submitPlan(planRequest)
```

#### 5. And you're done!

The plan will be executed as per the schedule:

- The action will be triggered every 7 seconds.
- The action will make a POST request to localhost with the JSON body specified.
- The request will have a timeout of 2 seconds.

---

## Plan Type

Karya supports the following plan types:

### One Time

This can be used to trigger a delayed action.

```kotlin
import karya.core.entities.PlanType

val oneTimePlan = PlanType.OneTime
```

### Recurring

This can be used to trigger an action periodically.

> **NOTE:** If the `endAt` field is not specified, the plan will run indefinitely.

```kotlin
planType = PlanType.Recurring(endAt = Instant.now().plusSeconds(30).toEpochMilli())
```

---

## Actions

Actions define what Karya should do once it has to execute the plan. The client supports the following actions:

> **NOTE:** Only the actions whose configs have been defined in the `executor.yml` file can be used. Read more about configuring different connectors in the executor [here](./CONNECTORS.md).

### REST API Request

<details>
<summary><strong>Make a REST API request to a specified URL with the given parameters.</strong></summary>


```kotlin
import karya.core.entities.Action
import karya.core.entities.PlanType
import karya.core.entities.http.Body
import karya.core.entities.http.Method
import karya.core.entities.http.Protocol

val apiRequest = Action.RestApiRequest(
  protocol = Protocol.HTTPS,
  baseUrl = "eox7wbcodh9parh.m.pipedream.net",
  method = Method.POST,
  headers = mapOf(
    "content-type" to "application/json",
    "client-header" to "Alice",
  ),
  body = Body.JsonBody(
    data = mapOf(
      "udf1" to "value",
      "udf2" to 1,
      "udf3" to true,
      "udf4" to 1.2,
      "udf5" to listOf(1, 2, 3, 4),
      "udf6" to mapOf(
        "nested-udf1" to listOf("a", "b", "c"),
        "nested-udf2" to mapOf("nested-nested-udf1" to true),
      ),
      "udf7" to listOf(
        mapOf("nested-udf3" to listOf(1, 2, 3)),
      ),
    ),
  ),
  timeout = 1000L,
)
```

</details>

### Push to Kafka

<details>

<summary><strong>Push a message to a Kafka topic.</strong></summary>

```kotlin
import karya.core.entities.Action

val kafkaAction = Action.KafkaProducerRequest(
  key = "partition-1",
  topic = "karya-test",
  message = "Published from executor",
)
```

</details>

### Send Email

<details>

<summary><strong>Send an email to a specified email address.</strong></summary>

```kotlin
import karya.core.entities.Action

val emailRequest = Action.EmailRequest(
  recipient = "recipient@gmail.com",
  subject = "Karya notification",
  message = "Hello from Karya!"
)
```

</details>

### Send a Slack Message

<details>

<summary><strong>Send a message to a specified Slack channel.</strong></summary>

```kotlin
import karya.core.entities.Action

// Define the Slack message to be sent periodically
val slackMessage = """[
    {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": "Hello, this is periodic slack message from Karya!"
        }
    }
]"""

Action.SlackMessageRequest(
  channel = "C083L324V99",
  message = slackMessage,
)
```

</details>

### Chain another job

<details>

<summary><strong>Chain another job to the current job.</strong></summary>

```kotlin
import karya.core.entities.Action

val chainedAction = Action.ChainedRequest(
  request = SubmitPlanRequest(
    userId = user.id,
    description = "Chained delay run",
    periodTime = "PT5S",
    planType = PlanType.Recurring(Instant.now().plusSeconds(30).toEpochMilli()),
    action = Action.RestApiRequest(
      baseUrl = "google.com",
    ),
  ),
)
```

</details>

---

## Hooks

Hooks are used to trigger actions on certain triggers. The client supports the following hooks:

| Event        | Description |
|--------------|-------------|
| `ON_FAILURE`   | Triggered when the plan fails to execute. |
| `ON_COMPLETION` | Triggered when the plan successfully completes. |

<details>

<summary><strong>Example of setting a hook on plan completion</strong></summary>

```kotlin
import karya.core.entities.Action
import karya.core.entities.Hook
import karya.core.entities.PlanType
import karya.core.entities.enums.Trigger

val completionHook = Hook(
  trigger = Trigger.ON_COMPLETION,
  action = Action.RestApiRequest(baseUrl = "http://localhost:35423"),
  maxRetry = 1
)

val planRequest = SubmitPlanRequest(
  userId = user.id,
  description = "Delay API call with completion hook",
  periodTime = "PT15S",
  planType = PlanType.OneTime,
  action = Action.RestApiRequest(
    baseUrl = "eox7wbcodh9parh.m.pipedream.net",
  ),
  hooks = listOf(
    completionHook
  )
)
```

</details>
