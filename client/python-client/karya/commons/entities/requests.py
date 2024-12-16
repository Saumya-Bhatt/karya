from typing import List, Optional
from karya.commons.entities.base import PlanType, ActionType
from karya.commons.entities.models.Hook import Hook
from dataclasses import dataclass, field

@dataclass
class CreateUserRequest:
    name: str

@dataclass
class SubmitPlanRequest:
    user_id: str
    description: str
    period_time: str
    plan_type: PlanType
    action: ActionType
    hooks: List[Hook] = field(default_factory=list)
    max_failure_retry: int = 3

@dataclass
class UpdatePlanRequest():
    planId: str
    periodTime: Optional[str]
    maxFailureRetry: Optional[int]
    hooks: Optional[List[Hook]]