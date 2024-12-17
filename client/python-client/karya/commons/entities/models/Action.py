from typing import Optional
from dataclasses import dataclass, field
from karya.commons.entities.base import ActionType, HttpBodyType
from karya.commons.entities.enums import Method, Protocol
from karya.commons.entities.models.HttpBody import EmptyBody

from typing import TYPE_CHECKING

# TYPE_CHECKING is used to prevent circular imports while providing hints during static analysis.
if TYPE_CHECKING:
    from karya.commons.entities.requests import SubmitPlanRequest


@dataclass
class RestApiRequest(ActionType):
    """
    Represents an HTTP request action that can be executed as part of a plan.

    Attributes:
        base_url (str): The base URL for the API request.
        body (HttpBodyType): The body of the HTTP request, represented as an HTTP body type.
        protocol (Protocol): The protocol used for the request (e.g., HTTP, HTTPS). Defaults to HTTP.
        method (Method): The HTTP method to be used (e.g., GET, POST, etc.). Defaults to GET.
        headers (dict): A dictionary of HTTP headers for the request. Defaults to content-type as application/json.
        timeout (int): The timeout duration for the request, in milliseconds. Defaults to 2000ms.
        type (str): The type of this action, which is `"karya.core.entities.Action.RestApiRequest"`.
    """

    base_url: str  # The base URL for the API request.
    body: HttpBodyType = field(
        default_factory=lambda: EmptyBody()
    )  # The body of the HTTP request (e.g., JSON payload).
    protocol: Protocol = (
        Protocol.HTTP
    )  # The protocol (HTTP/HTTPS) used for the request.
    method: Method = (
        Method.GET
    )  # The HTTP method used for the request (GET, POST, etc.).
    headers: dict = field(
        default_factory=lambda: {"content-type": "application/json"}
    )  # Default headers for the request.
    timeout: int = 2000  # Timeout for the request in milliseconds.
    type: str = "karya.core.entities.Action.RestApiRequest"  # The type identifier for this action.


@dataclass
class SlackMessageRequest(ActionType):
    """
    Represents an action that sends a message to a Slack channel.

    Attributes:
        channel (str): The Slack channel where the message will be sent.
        message (str): The content of the Slack message.
        type (str): The type of this action, which is `"karya.core.entities.Action.SlackMessageRequest"`.
    """

    channel: str  # The Slack channel to send the message to.
    message: str  # The message content.
    type: str = "karya.core.entities.Action.SlackMessageRequest"  # The type identifier for this action.


@dataclass
class EmailRequest(ActionType):
    """
    Represents an action to send an email.

    Attributes:
        recipient (str): The recipient's email address.
        subject (str): The subject of the email.
        message (str): The body content of the email.
        type (str): The type of this action, which is `"karya.core.entities.Action.EmailRequest"`.
    """

    recipient: str  # The recipient of the email.
    subject: str  # The subject of the email.
    message: str  # The content of the email body.
    type: str = "karya.core.entities.Action.EmailRequest"  # The type identifier for this action.


@dataclass
class KafkaProducerRequest(ActionType):
    """
    Represents an action that publishes a message to a Kafka topic.

    Attributes:
        topic (str): The Kafka topic where the message will be published.
        message (str): The content of the Kafka message.
        key (Optional[str]): The optional key for the Kafka message, used for partitioning.
        type (str): The type of this action, which is `"karya.core.entities.Action.KafkaProducerRequest"`.
    """

    topic: str  # The Kafka topic where the message will be published.
    message: str  # The content of the message to send.
    key: Optional[str] = field(
        default=None
    )  # An optional key for the Kafka message, used for partitioning.
    type: str = "karya.core.entities.Action.KafkaProducerRequest"  # The type identifier for this action.


@dataclass
class ChainedRequest(ActionType):
    """
    Represents an action that chains multiple requests into a single action.

    Attributes:
        request (SubmitPlanRequest): The request that is part of the chain. This can be another plan submission request.
        type (str): The type of this action, which is `"karya.core.entities.Action.ChainedRequest"`.
    """

    request: (
        "SubmitPlanRequest"  # The plan request that is part of the chained actions.
    )
    type: str = "karya.core.entities.Action.ChainedRequest"  # The type identifier for this action.
