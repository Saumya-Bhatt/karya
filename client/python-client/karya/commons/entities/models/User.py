from dataclasses import dataclass


@dataclass
class User:
    id: str
    name: str
    created_at: int
