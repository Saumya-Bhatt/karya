from karya.commons.entities.base import ActionType
from karya.commons.entities.enums import Trigger
from dataclasses import dataclass


@dataclass
class Hook:
    """
    Represents a hook that can be triggered during the execution of a plan.

    A hook consists of a trigger condition (e.g., on failure or completion) and the action to be performed
    when that condition is met. Additionally, it defines a maximum retry count in case the action fails.

    Attributes:
        trigger (Trigger): The condition under which the hook should be triggered.
        action (ActionType): The action to be executed when the hook is triggered.
        max_retry (int): The maximum number of retries allowed if the action fails. Defaults to 3.
    """

    trigger: Trigger  # The condition under which the hook will be triggered (e.g., ON_FAILURE).
    action: ActionType  # The action to be executed when the hook is triggered (e.g., sending an API request).
    max_retry: int = (
        3  # The maximum number of retries in case of failure. Defaults to 3.
    )
