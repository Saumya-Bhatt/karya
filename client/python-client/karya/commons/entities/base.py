from abc import ABC

# Base classes for different types used within the system


class ErrorLogType(ABC):
    """
    Abstract base class for defining different types of error logs.

    This class serves as a base for all error log types, which can be extended
    to include specific implementations for handling different types of error logs
    within the system.
    """

    pass


class PlanType(ABC):
    """
    Abstract base class for defining different types of plans.

    This class acts as a parent class for all plan types (e.g., recurring plans, one-time plans).
    Specific plan types should inherit from this class to provide custom behavior or attributes.
    """

    pass


class ActionType(ABC):
    """
    Abstract base class for defining different types of actions.

    This class serves as the base for various action types (e.g., API requests, email actions, Kafka actions).
    Each specific action type will inherit from this class to implement the necessary functionality.
    """

    pass


class HttpBodyType(ABC):
    """
    Abstract base class for defining HTTP request body types.

    This class is intended to be extended by specific body types (e.g., JSON, form data).
    It standardizes the format for request bodies in HTTP operations, ensuring a consistent structure
    for different types of HTTP requests.
    """

    pass
