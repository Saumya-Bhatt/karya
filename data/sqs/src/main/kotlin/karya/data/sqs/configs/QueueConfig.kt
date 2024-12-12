package karya.data.sqs.configs

import karya.core.utils.readValue

data class QueueConfig(
  val executorQueueUrl: String,
  val hooksQueueUrl: String,
  val deadLetterQueueUrl: String,
  val maxMessageFetched: Int,
  val longPollingWaitTime: Int,
  val visibilityTimeout: Int,
) {
  constructor(props: Map<*, *>) : this(
    executorQueueUrl = props.readValue("executorQueueUrl"),
    hooksQueueUrl = props.readValue("hooksQueueUrl"),
    deadLetterQueueUrl = props.readValue("deadLetterQueueUrl"),
    maxMessageFetched = props.readValue("maxMessageFetched"),
    longPollingWaitTime = props.readValue("longPollingWaitTime"),
    visibilityTimeout = props.readValue("visibilityTimeout"),
  )
}
