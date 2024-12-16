from typing import Optional
from dataclasses import dataclass, field
from karya.commons.entities.base import ActionType, HttpBodyType
from karya.commons.entities.enums import Method, Protocol

from typing import TYPE_CHECKING
if TYPE_CHECKING:
    from karya.commons.entities.requests import SubmitPlanRequest

@dataclass
class RestApiRequest(ActionType):
    base_url: str
    body: HttpBodyType
    protocol: Protocol = Protocol.HTTP
    method: Method = Method.GET
    headers: dict = field(default_factory=lambda x : {"content-type": "application/json"})
    timeout: int = 2000
    type: str = "karya.core.entities.Action.RestApiRequest"


@dataclass
class SlackMessageRequest(ActionType):
    channel: str
    message: str
    type: str = "karya.core.entities.Action.SlackMessageRequest"


@dataclass
class EmailRequest(ActionType):
    recipient: str
    subject: str
    message: str
    type: str = "karya.core.entities.Action.EmailRequest"


@dataclass
class KafkaProducerRequest(ActionType):
    key: Optional[str]
    topic: str
    message: str
    type: str = "karya.core.entities.Action.KafkaProducerRequest"


@dataclass
class ChainedRequest(ActionType):
    request: "SubmitPlanRequest"
    type: str = "karya.core.entities.Action.ChainedRequest"
