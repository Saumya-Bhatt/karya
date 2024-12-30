package karya.core.repos

import karya.core.entities.Plan
import karya.core.entities.enums.PlanStatus
import java.util.*

/**
 * Interface representing a repository for managing plans.
 */
interface PlansRepo {

  /**
   * Adds a new plan to the repository.
   *
   * @param plan The plan to be added.
   */
  suspend fun add(plan: Plan)

  /**
   * Retrieves a plan by its unique identifier.
   *
   * @param id The unique identifier of the plan.
   * @return The plan associated with the specified identifier, or `null` if not found.
   */
  suspend fun get(id: UUID): Plan?

  /**
   * Updates an existing plan in the repository.
   *
   * @param plan The plan with updated information.
   */
  suspend fun update(plan: Plan)

  /**
   * Updates the status of a plan.
   *
   * @param id The unique identifier of the plan.
   * @param status The new status to be set for the plan.
   */
  suspend fun updateStatus(id: UUID, status: PlanStatus)

  /**
   * Retrieves the list of child plan identifiers for a given plan.
   *
   * @param id The unique identifier of the parent plan.
   * @return A list of unique identifiers of the child plans.
   */
  suspend fun getChildPlanIds(id: UUID): List<UUID>

  suspend fun getAll(userId: UUID, offset: Long, size: Int): List<Plan>
}
