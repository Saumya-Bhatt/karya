from karya.commons.entities.enums import TaskStatus
from typing import Optional
from dataclasses import dataclass

@dataclass
class Task:
    id: str
    plan_id: str
    partition_key: int
    status: TaskStatus
    created_at: int
    executed_at: Optional[int]
    next_execution_at: Optional[int]