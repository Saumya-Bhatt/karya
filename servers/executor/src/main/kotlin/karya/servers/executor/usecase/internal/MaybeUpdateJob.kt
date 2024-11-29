package karya.servers.executor.usecase.internal

import karya.core.entities.JobType
import karya.core.entities.enums.JobStatus
import karya.core.exceptions.JobException.JobNotFoundException
import karya.core.repos.JobsRepo
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

class MaybeUpdateJob
@Inject
constructor(
  private val jobsRepo: JobsRepo
) {

  companion object : Logging

  suspend fun invoke(jobId: UUID) {
    val job = jobsRepo.get(jobId) ?: throw JobNotFoundException(jobId)
    when (val type = job.type) {
      is JobType.Recurring -> if (type.isEnded()) markJobCompleted(jobId) else return
      is JobType.OneTime -> markJobCompleted(jobId)
    }
    logger.info("[JOB COMPLETED] --- jobId : $jobId")
  }

  private suspend fun markJobCompleted(jobId: UUID) =
    jobsRepo.updateStatus(jobId, JobStatus.COMPLETED)
}