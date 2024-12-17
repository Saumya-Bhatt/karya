from typing import List, Optional
from karya.commons.entities.base import PlanType, ActionType
from karya.commons.entities.models.Hook import Hook
from dataclasses import dataclass, field

# Data classes used for creating, submitting, and updating plans, along with user creation requests.


@dataclass
class CreateUserRequest:
    """
    Represents a request to create a new user.

    Attributes:
        name (str): The name of the user to be created.
    """

    name: str


@dataclass
class SubmitPlanRequest:
    """
    Represents a request to submit a plan for a user.

    Attributes:
        user_id (str): The ID of the user for whom the plan is being submitted.
        description (str): A description of the plan.
        period_time (str): The period time (e.g., "PT1H" for 1 hour) during which the plan runs.
        plan_type (PlanType): The type of plan (e.g., recurring or one-time).
        action (ActionType): The action to be performed as part of the plan (e.g., API call, email).
        hooks (List[Hook]): A list of hooks to be executed on specified conditions (e.g., on failure, on completion).
        max_failure_retry (int): The maximum number of retries if the plan or task fails. Default is 3 retries.
    """

    user_id: str
    description: str
    period_time: str
    plan_type: PlanType
    action: ActionType
    hooks: List[Hook] = field(
        default_factory=list
    )  # Default is an empty list if no hooks are provided
    max_failure_retry: int = 3  # Default is 3 retries


@dataclass
class UpdatePlanRequest:
    """
    Represents a request to update an existing plan.

    Attributes:
        planId (str): The ID of the plan to be updated.
        periodTime (Optional[str]): The new period time for the plan (e.g., "PT1H"). Can be `None` to leave unchanged.
        maxFailureRetry (Optional[int]): The new maximum number of retries for the plan. Can be `None` to leave unchanged.
        hooks (Optional[List[Hook]]): The new list of hooks to be associated with the plan. Can be `None` to leave unchanged.
    """

    planId: str
    periodTime: Optional[str]  # Optional field, can be None if no update is needed
    maxFailureRetry: Optional[int]  # Optional field, can be None if no update is needed
    hooks: Optional[List[Hook]]  # Optional field, can be None if no update is needed
