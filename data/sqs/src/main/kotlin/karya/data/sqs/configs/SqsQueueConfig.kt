package karya.data.sqs.configs

import karya.core.configs.QueueConfig as KaryaQueueConfig

data class SqsQueueConfig(
  val overrideLocalUrl: String,
  val queueConfig: QueueConfig,
  val awsConfig: AwsConfig
) : KaryaQueueConfig("sqs") {

  companion object {
    const val SQS_IDENTIFIER = "sqs"
  }

  constructor(props: Map<*, *>) : this(
    overrideLocalUrl = props["overrideLocalUrl"] as String,
    queueConfig = QueueConfig(props["queueConfig"] as Map<*, *>),
    awsConfig = AwsConfig(props["awsConfig"] as Map<*, *>)
  )
}
