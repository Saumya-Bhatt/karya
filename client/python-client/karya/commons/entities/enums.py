from enum import Enum


# Enum class to represent different triggers for actions in a workflow.
class Trigger(str, Enum):
    """
    Enum that defines the possible triggers for an action to occur within a plan or workflow.

    - `ON_FAILURE`: The action is triggered when the plan fails.
    - `ON_COMPLETION`: The action is triggered when the plan successfully completes.
    """

    ON_FAILURE = "ON_FAILURE"
    ON_COMPLETION = "ON_COMPLETION"


# Enum class to represent different protocols used for HTTP communication.
class Protocol(str, Enum):
    """
    Enum that defines the possible protocols for HTTP communication.

    - `HTTP`: The standard HTTP protocol.
    - `HTTPS`: The secure version of HTTP, encrypted using SSL/TLS.
    """

    HTTP = "HTTP"
    HTTPS = "HTTPS"


# Enum class to represent HTTP request methods.
class Method(str, Enum):
    """
    Enum that defines the HTTP methods used for making requests.

    - `GET`: Retrieve data from the server.
    - `POST`: Send data to the server.
    - `PATCH`: Partially update data on the server.
    - `DELETE`: Remove data from the server.
    """

    GET = "GET"
    POST = "POST"
    PATCH = "PATCH"
    DELETE = "DELETE"


# Enum class to represent the different statuses of a plan.
class PlanStatus(Enum):
    """
    Enum that defines the possible states a plan can be in during its lifecycle.

    - `CREATED`: The plan has been created but not yet started.
    - `RUNNING`: The plan is currently in execution.
    - `COMPLETED`: The plan has finished execution.
    - `CANCELLED`: The plan was cancelled before completion.
    """

    CREATED = "CREATED"
    RUNNING = "RUNNING"
    COMPLETED = "COMPLETED"
    CANCELLED = "CANCELLED"


# Enum class to represent the different statuses of a task within a plan.
class TaskStatus(Enum):
    """
    Enum that defines the possible states of a task during its execution within a plan.

    - `CREATED`: The task has been created but not yet processed.
    - `PROCESSING`: The task is currently being processed.
    - `SUCCESS`: The task was processed successfully.
    - `FAILURE`: The task encountered an error and failed.
    - `CANCELLED`: The task was cancelled before completion.
    """

    CREATED = "CREATED"
    PROCESSING = "PROCESSING"
    SUCCESS = "SUCCESS"
    FAILURE = "FAILURE"
    CANCELLED = "CANCELLED"
