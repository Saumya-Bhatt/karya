package karya.data.sqs.utils

import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider

class KaryaAwsCredentialsProvider : AwsCredentialsProvider {

  override fun resolveCredentials(): AwsCredentials? {
    TODO("Not yet implemented")
  }
}
