package karya.servers.executor.usecase.internal

import karya.core.entities.ExecutorResult
import karya.core.entities.ExecutorResult.Failure
import karya.core.entities.ExecutorResult.Success
import karya.core.entities.Hook
import karya.core.entities.Plan
import karya.core.entities.PlanType
import karya.core.entities.enums.Trigger
import karya.core.entities.enums.Trigger.ON_COMPLETION
import karya.core.entities.enums.Trigger.ON_FAILURE
import karya.core.queues.ProducerQueueClient
import karya.core.queues.entities.QueueMessage
import karya.core.queues.entities.QueueType
import org.apache.logging.log4j.kotlin.Logging
import java.util.*
import javax.inject.Inject

/**
 * Use case class responsible for triggering hooks based on the executor result.
 *
 * @property queueClient The producer client for interacting with the queue.
 * @constructor Creates an instance of [TriggerHook] with the specified queue client.
 */
class TriggerHook
@Inject
constructor(
  private val queueClient: ProducerQueueClient
) {

  companion object : Logging

  /**
   * Triggers the appropriate hook based on the executor result.
   *
   * @param plan The plan associated with the hook.
   * @param hook The hook to be triggered.
   * @param result The result of the executor's operation.
   */
  suspend fun invoke(plan: Plan, hook: Hook, result: ExecutorResult) {
    when (result) {
      is Success -> if (shouldTriggerOnCompletion(plan.type, hook.trigger)) pushToHookQueue(plan.id, hook)
      is Failure -> if (shouldTriggerOnFailure(hook.trigger)) pushToHookQueue(plan.id, hook)
    }
  }

  /**
   * Determines if the hook should be triggered on completion.
   *
   * @param planType The type of the plan.
   * @param trigger The trigger condition for the hook.
   * @return True if the hook should be triggered on completion, false otherwise.
   */
  private fun shouldTriggerOnCompletion(planType: PlanType, trigger: Trigger) = when (planType) {
    is PlanType.OneTime -> trigger == ON_COMPLETION
    is PlanType.Recurring -> planType.isEnded().and(trigger == ON_COMPLETION)
  }.also { logger.info("[HOOK] --- shouldTriggerOnCompletion : $it") }

  /**
   * Determines if the hook should be triggered on failure.
   *
   * @param trigger The trigger condition for the hook.
   * @return True if the hook should be triggered on failure, false otherwise.
   */
  private fun shouldTriggerOnFailure(trigger: Trigger) =
    (trigger == ON_FAILURE).also { logger.info("[HOOK] --- shouldTriggerOnFailure : $it") }

  /**
   * Pushes the hook message to the hook queue.
   *
   * @param planId The unique identifier of the plan.
   * @param hook The hook to be pushed to the queue.
   */
  private suspend fun pushToHookQueue(planId: UUID, hook: Hook) =
    QueueMessage.HookMessage(planId = planId, hook = hook).let { queueClient.push(it, QueueType.HOOK) }

}
