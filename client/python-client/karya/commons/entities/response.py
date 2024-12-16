from karya.commons.entities.models.Plan import Plan
from karya.commons.entities.models.Task import Task
from karya.commons.entities.models.ErrorLog import ErrorLog
from dataclasses import dataclass
from typing import List


@dataclass
class GetPlanResponse:
    plan: Plan
    latest_task: Task

@dataclass
class GetSummaryResponse:
    plan: Plan
    tasks: List[Task]
    error_logs: List[ErrorLog]