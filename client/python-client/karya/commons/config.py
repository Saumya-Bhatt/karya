from dataclasses import dataclass
from karya.commons.entities.enums import Protocol


@dataclass
class ClientConfig:
    protocol: Protocol
    host: str
    port: int

    def get_base_url(self):
        return f"{self.protocol.value}://{self.host}:{self.port}"

    def dev():
        return ClientConfig(Protocol.HTTP, "localhost", 8080)
