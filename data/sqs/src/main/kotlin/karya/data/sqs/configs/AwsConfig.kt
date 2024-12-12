package karya.data.sqs.configs

import karya.core.utils.readValue

data class AwsConfig(
  val region: String,
  val accessKeyId: String,
  val secretAccessKey: String
) {
  constructor(props: Map<*, *>) : this(
    region = props.readValue("region"),
    accessKeyId = props.readValue("accessKeyId"),
    secretAccessKey = props.readValue("secretAccessKey")
  )
}
