from karya.commons.entities.models.Plan import Plan
from karya.commons.entities.models.Task import Task
from karya.commons.entities.models.ErrorLog import ErrorLog
from dataclasses import dataclass
from typing import List

# Data classes used to structure responses related to plans, tasks, and error logs.


@dataclass
class GetPlanResponse:
    """
    Represents the response returned when querying a specific plan.

    Attributes:
        plan (Plan): The plan details, encapsulated as a Plan object.
        latest_task (Task): The most recent task associated with the plan, encapsulated as a Task object.
    """

    plan: Plan  # The plan object containing details about the plan.
    latest_task: Task  # The most recent task related to the plan, which may contain the latest status or result.


@dataclass
class GetSummaryResponse:
    """
    Represents the response returned when querying a summary of a plan.

    Attributes:
        plan (Plan): The plan details, encapsulated as a Plan object.
        tasks (List[Task]): A list of tasks associated with the plan.
        error_logs (List[ErrorLog]): A list of error logs that were generated during the execution of the plan.
    """

    plan: Plan  # The plan object containing detailed information about the plan.
    tasks: List[
        Task
    ]  # A list of Task objects associated with the plan. Each task represents an individual operation within the plan.
    error_logs: List[
        ErrorLog
    ]  # A list of error logs generated during the execution of the plan. Each error log provides details of a failure or issue.
