from typing import List, Optional
from karya.commons.entities.base import PlanType, ActionType
from karya.commons.entities.enums import PlanStatus
from karya.commons.entities.models.Hook import Hook
from dataclasses import dataclass


@dataclass
class Recurring(PlanType):
    """
    Represents a recurring plan type.

    This class is used for plans that repeat at regular intervals. It includes an optional `end_at` field that
    specifies when the plan will stop recurring. If `end_at` is `None`, the plan will continue indefinitely.

    Attributes:
        end_at (Optional[str]): The end date/time for the recurring plan in ISO 8601 format. If `None`, the plan continues indefinitely.
        type (str): The type of the plan, default value is "karya.core.entities.PlanType.Recurring".
    """

    end_at: Optional[
        str
    ]  # The end time for the recurring plan, or None for an indefinite recurring plan.
    type: str = "karya.core.entities.PlanType.Recurring"  # The type identifier for a recurring plan.


@dataclass
class OneTime(PlanType):
    """
    Represents a one-time plan type.

    This class is used for plans that execute only once and do not repeat. Once the plan completes, it will not execute again.

    Attributes:
        type (str): The type of the plan, default value is "karya.core.entities.PlanType.OneTime".
    """

    type: str = "karya.core.entities.PlanType.OneTime"  # The type identifier for a one-time plan.


@dataclass
class Plan:
    """
    Represents a plan that a user can create and execute.

    A plan is a set of instructions or actions to be executed at a specified interval or on a one-time basis.
    It includes metadata such as plan description, status, failure retries, associated actions, and hooks for additional logic.

    Attributes:
        id (str): The unique identifier of the plan.
        user_id (str): The ID of the user who created the plan.
        description (str): A brief description of the plan.
        period_time (str): The time interval between executions (ISO 8601 format).
        type (PlanType): The type of plan, either recurring or one-time.
        status (PlanStatus): The current status of the plan (e.g., created, running, completed, cancelled).
        max_failure_retry (int): The maximum number of retries allowed if the plan fails.
        action (ActionType): The action to be performed as part of the plan.
        hook (List[Hook]): A list of hooks that can trigger additional actions based on plan events (e.g., on failure).
        parent_plan_id (Optional[str]): The ID of the parent plan, if this plan is a child or sub-plan.
        created_at (int): The timestamp when the plan was created.
        updated_at (int): The timestamp when the plan was last updated.
    """

    id: str  # Unique identifier for the plan.
    user_id: str  # The user who created the plan.
    description: str  # A short description of the plan.
    period_time: (
        str  # The period or frequency of the plan's execution in ISO 8601 format.
    )
    type: PlanType  # The type of the plan (either recurring or one-time).
    status: PlanStatus  # The current status of the plan (e.g., created, running, completed, cancelled).
    max_failure_retry: int  # The maximum number of retries allowed if the plan fails.
    action: ActionType  # The action that the plan will execute (e.g., REST API call, Slack message).
    hook: List[
        Hook
    ]  # List of hooks that trigger actions based on events (e.g., on failure, on completion).
    parent_plan_id: Optional[
        str
    ]  # The ID of the parent plan if this plan is part of a larger plan.
    created_at: int  # The timestamp indicating when the plan was created.
    updated_at: int  # The timestamp indicating when the plan was last updated.
