from dataclasses import dataclass


@dataclass
class User:
    """
    Represents a user in the system.

    The `User` class contains basic information about a user, including their unique identifier,
    name, and the timestamp of when they were created in the system.

    Attributes:
        id (str): The unique identifier for the user.
        name (str): The name of the user.
        created_at (int): The timestamp indicating when the user was created (Unix timestamp).
    """

    id: str  # Unique identifier for the user, typically generated when the user is created.
    name: str  # The name of the user.
    created_at: (
        int  # The timestamp when the user was created (in Unix timestamp format).
    )
