from karya.commons.entities.base import ErrorLogType
from dataclasses import dataclass


@dataclass
class HookErrorLog(ErrorLogType):
    pass


@dataclass
class ExecutorErrorLog(ErrorLogType):
    task_id: str


@dataclass
class ErrorLog:
    plan_id: str
    error: str
    type: ErrorLogType
    timestamp: int
