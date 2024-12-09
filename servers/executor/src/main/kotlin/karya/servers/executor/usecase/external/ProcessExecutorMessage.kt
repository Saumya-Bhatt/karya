package karya.servers.executor.usecase.external

import karya.core.entities.Plan
import karya.core.entities.PlanType
import karya.core.entities.enums.PlanStatus
import karya.core.exceptions.KaryaException
import karya.core.exceptions.PlanException
import karya.core.queues.ProducerQueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import karya.core.repos.PlansRepo
import karya.servers.executor.usecase.internal.GetConnector
import karya.servers.executor.usecase.internal.ProcessTask
import karya.servers.executor.usecase.internal.TriggerHook
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

/**
 * Use case class responsible for processing executor messages.
 *
 * @property queueClient The producer client for interacting with the queue.
 * @property plansRepo The repository for managing plans.
 * @property processTask The use case for processing tasks.
 * @property triggerHook The use case for triggering hooks.
 * @property getConnector The use case for retrieving connectors.
 * @constructor Creates an instance of [ProcessExecutorMessage] with the specified dependencies.
 */
class ProcessExecutorMessage
@Inject
constructor(
  private val queueClient: ProducerQueueClient,
  private val plansRepo: PlansRepo,
  private val processTask: ProcessTask,
  private val triggerHook: TriggerHook,
  private val getConnector: GetConnector
) {

  companion object : Logging

  /**
   * Processes the executor message.
   *
   * @param message The executor message to be processed.
   */
  suspend fun invoke(message: QueueMessage.ExecutorMessage) = try {
    val connector = getConnector.invoke(message.action)
    val result = connector.invoke(message.planId, message.action)
    val plan = plansRepo.get(message.planId) ?: throw PlanException.PlanNotFoundException(message.planId)

    processTask.invoke(result, message)
    processPlan(plan)
    plan.hook.forEach { triggerHook.invoke(plan, it, result) }

  } catch (e: KaryaException) {
    logger.error(e) { "[PUSHING MESSAGE TO DLQ] Exception while executing action --- ${e.message}" }
    queueClient.push(message, QueueType.DEAD_LETTER)
  }

  /**
   * Processes the plan based on its type.
   *
   * @param plan The plan to be processed.
   */
  private suspend fun processPlan(plan: Plan) {
    when (val type = plan.type) {
      is PlanType.Recurring -> if (type.isEnded()) markPlanCompleted(plan.id) else return
      is PlanType.OneTime -> markPlanCompleted(plan.id)
    }
    logger.info("[PLAN COMPLETED] --- planId : $plan.id")
  }

  /**
   * Marks the plan as completed.
   *
   * @param planId The unique identifier of the plan to be marked as completed.
   */
  private suspend fun markPlanCompleted(planId: UUID) =
    plansRepo.updateStatus(planId, PlanStatus.COMPLETED)
}
