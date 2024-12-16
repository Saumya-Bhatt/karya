from karya.commons.entities.base import ActionType
from karya.commons.entities.enums import Trigger

from dataclasses import dataclass


@dataclass
class Hook:
    trigger: Trigger
    action: ActionType
    max_retry: int = 3
