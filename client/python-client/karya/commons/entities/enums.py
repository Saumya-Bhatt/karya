from enum import Enum


class Trigger(str, Enum):
    ON_FAILURE = "ON_FAILURE"
    ON_COMPLETION = "ON_COMPLETION"

class Protocol(str, Enum):
    HTTP = "HTTP"
    HTTPS = "HTTPS"

class Method(str, Enum):
    GET = "GET"
    POST = "POST"
    PATCH = "PATCH"
    DELETE = "DELETE"

class PlanStatus(Enum):
    CREATED = "CREATED"
    RUNNING = "RUNNING"
    COMPLETED = "COMPLETED"
    CANCELLED = "CANCELLED"

class TaskStatus(Enum):
    CREATED = "CREATED"
    PROCESSING = "PROCESSING"
    SUCCESS = "SUCCESS"
    FAILURE = "FAILURE"
    CANCELLED = "CANCELLED"