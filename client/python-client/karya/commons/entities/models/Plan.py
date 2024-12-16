from typing import List, Optional
from karya.commons.entities.base import PlanType, ActionType
from karya.commons.entities.enums import PlanStatus
from karya.commons.entities.models.Hook import Hook
from dataclasses import dataclass

@dataclass
class Recurring(PlanType):
    end_at: Optional[str]
    type: str = "karya.core.entities.PlanType.Recurring"

@dataclass
class OneTime(PlanType):
    type: str = "karya.core.entities.PlanType.OneTime"

@dataclass
class Plan:
    id: str
    user_id: str
    description: str
    period_time: str
    type: PlanType
    status: PlanStatus
    max_failure_retry: int
    action: ActionType
    hook: List[Hook]
    parent_plan_id: Optional[str]
    created_at: int
    updated_at: int
