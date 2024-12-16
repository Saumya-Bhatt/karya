from typing import Any, Dict
import json
from karya.commons.entities.base import HttpBodyType
from dataclasses import dataclass

@dataclass
class JsonBody(HttpBodyType):
    json_string: str
    type: str = "karya.core.entities.http.Body.JsonBody"

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> "JsonBody":
        return cls(json_string=json.dumps(data))

@dataclass
class EmptyBody(HttpBodyType):
    type: str = "karya.core.entities.http.Body.EmptyBody"
