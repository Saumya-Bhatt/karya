from dataclasses import dataclass
from karya.commons.entities.enums import Protocol


@dataclass
class ClientConfig:
    """
    A configuration class for a Karya API client.

    This class holds the necessary configuration parameters to connect to the Karya API,
    including the communication protocol, host, and port. It also provides methods for
    generating the base URL and for returning a default development configuration.

    Attributes:
        protocol (Protocol): The communication protocol (e.g., HTTP or HTTPS).
        host (str): The host or domain name of the API server.
        port (int): The port number used to access the API.

    Methods:
        get_base_url() -> str:
            Generates and returns the base URL for the API based on the configuration.
        dev() -> ClientConfig:
            Returns a default configuration intended for local development.
    """

    protocol: Protocol
    host: str
    port: int

    def get_base_url(self) -> str:
        """
        Generates and returns the base URL for the API.

        The base URL is constructed by combining the protocol, host, and port attributes
        of the ClientConfig. This is useful for constructing full URLs for API requests.

        Returns:
            str: The base URL of the Karya API in the format "<protocol>://<host>:<port>".

        Example:
            If the configuration is:
                protocol = Protocol.HTTP
                host = "localhost"
                port = 8080
            The returned base URL will be "http://localhost:8080".
        """
        return f"{self.protocol.value}://{self.host}:{self.port}"

    @staticmethod
    def dev() -> "ClientConfig":
        """
        Returns a default configuration for development purposes.

        This method returns a configuration that uses HTTP as the protocol, "localhost" as the host,
        and port 8080. It is intended for use in local development environments where the API is
        running locally.

        Returns:
            ClientConfig: A ClientConfig instance with predefined values for development.

        Example:
            ClientConfig.dev() returns:
            ClientConfig(protocol=Protocol.HTTP, host="localhost", port=8080)
        """
        return ClientConfig(Protocol.HTTP, "localhost", 8080)
