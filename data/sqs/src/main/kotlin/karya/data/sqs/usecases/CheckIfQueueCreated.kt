package karya.data.sqs.usecases

import org.apache.logging.log4j.kotlin.Logging
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException
import javax.inject.Inject

class CheckIfQueueCreated
@Inject
constructor(
  private val sqsClient: SqsClient
) {

  companion object: Logging

  // In SQS, queue creation is typically done via CloudFormation or AWS CLI
  // Here you would ensure queues exist or create them if needed
  fun invoke(queueUrl: String) = try {
    val request = GetQueueAttributesRequest.builder()
      .queueUrl(queueUrl)
      .build()
    sqsClient.getQueueAttributes(request)
    logger.info("Queue $queueUrl exists")

  } catch (e: QueueDoesNotExistException) {
    throw IllegalStateException("Queue $queueUrl does not exist")
  }
}
