from karya.commons.entities.base import ErrorLogType
from dataclasses import dataclass


@dataclass
class HookErrorLog(ErrorLogType):
    """
    Represents an error log specific to a hook execution.

    Inherits from `ErrorLogType`, allowing it to be categorized as a type of error log.

    Attributes:
        (None, as this class does not have additional attributes beyond those from `ErrorLogType`.)
    """

    pass  # Inherits from `ErrorLogType`, no additional attributes.


@dataclass
class ExecutorErrorLog(ErrorLogType):
    """
    Represents an error log specific to an executor execution.

    Inherits from `ErrorLogType` and includes additional information about the task that caused the error.

    Attributes:
        task_id (str): The ID of the task associated with the error.
    """

    task_id: str  # The ID of the task that encountered the error.


@dataclass
class ErrorLog:
    """
    Represents a generic error log that tracks errors encountered during the execution of a plan.

    Attributes:
        plan_id (str): The ID of the plan that encountered the error.
        error (str): A description of the error that occurred.
        type (ErrorLogType): The type of error log (either `HookErrorLog` or `ExecutorErrorLog`).
        timestamp (int): The timestamp (in Unix format) when the error occurred.
    """

    plan_id: str  # The ID of the plan associated with the error.
    error: str  # A description of the error.
    type: ErrorLogType  # The type of the error log (either hook or executor error).
    timestamp: int  # The timestamp when the error occurred (in Unix format).
