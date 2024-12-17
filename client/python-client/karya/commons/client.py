from abc import ABC, abstractmethod
from karya.commons.entities.models.Plan import Plan
from karya.commons.entities.models.User import User
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
    UpdatePlanRequest,
)
from karya.commons.entities.response import (
    GetPlanResponse,
    GetSummaryResponse,
)
import uuid


class Client(ABC):
    """
    Abstract base class for a client interacting with the Karya API.

    This class defines the common interface for a Karya client, providing methods
    for user and plan management. Concrete subclasses should implement these
    methods to interact with the actual API, typically over HTTP.

    Methods:
        create_user(request: CreateUserRequest) -> User:
            Creates a new user in the system.
        submit_plan(request: SubmitPlanRequest) -> Plan:
            Submits a new plan to the system.
        get_plan(plan_id: uuid.UUID) -> GetPlanResponse:
            Retrieves the details of a specific plan by its ID.
        update_plan(request: UpdatePlanRequest) -> Plan:
            Updates an existing plan with new information.
        cancel_plan(plan_id: uuid.UUID) -> Plan:
            Cancels an existing plan by its ID.
        get_summary(plan_id: uuid.UUID) -> GetSummaryResponse:
            Retrieves the summary of a specific plan.
        close() -> None:
            Closes any active connections or resources held by the client.
    """

    @abstractmethod
    async def create_user(self, request: CreateUserRequest) -> User:
        """
        Creates a new user in the system.

        Args:
            request (CreateUserRequest): The request object containing data for the new user.

        Returns:
            User: A User object representing the newly created user.

        This method should be implemented by a concrete subclass to interact with
        the actual user creation endpoint in the API.
        """
        pass

    @abstractmethod
    async def submit_plan(self, request: SubmitPlanRequest) -> Plan:
        """
        Submits a new plan to the system.

        Args:
            request (SubmitPlanRequest): The request object containing data for the new plan.

        Returns:
            Plan: A Plan object representing the newly submitted plan.

        This method should be implemented by a concrete subclass to interact with
        the actual plan submission endpoint in the API.
        """
        pass

    @abstractmethod
    async def get_plan(self, plan_id: uuid.UUID) -> GetPlanResponse:
        """
        Retrieves the details of a specific plan by its ID.

        Args:
            plan_id (uuid.UUID): The unique identifier of the plan to retrieve.

        Returns:
            GetPlanResponse: A response object containing the details of the plan.

        This method should be implemented by a concrete subclass to interact with
        the actual plan retrieval endpoint in the API.
        """
        pass

    @abstractmethod
    async def update_plan(self, request: UpdatePlanRequest) -> Plan:
        """
        Updates an existing plan with new data.

        Args:
            request (UpdatePlanRequest): The request object containing the updated data for the plan.

        Returns:
            Plan: A Plan object representing the updated plan.

        This method should be implemented by a concrete subclass to interact with
        the actual plan update endpoint in the API.
        """
        pass

    @abstractmethod
    async def cancel_plan(self, plan_id: uuid.UUID) -> Plan:
        """
        Cancels an existing plan by its ID.

        Args:
            plan_id (uuid.UUID): The unique identifier of the plan to cancel.

        Returns:
            Plan: A Plan object reflecting the canceled plan.

        This method should be implemented by a concrete subclass to interact with
        the actual plan cancellation endpoint in the API.
        """
        pass

    @abstractmethod
    async def get_summary(self, plan_id: uuid.UUID) -> GetSummaryResponse:
        """
        Retrieves the summary of a specific plan by its ID.

        Args:
            plan_id (uuid.UUID): The unique identifier of the plan to retrieve a summary for.

        Returns:
            GetSummaryResponse: A response object containing the summary of the plan.

        This method should be implemented by a concrete subclass to interact with
        the actual plan summary retrieval endpoint in the API.
        """
        pass

    @abstractmethod
    async def close(self) -> None:
        """
        Closes any active connections or resources held by the client.

        This method should be implemented by a concrete subclass to ensure any
        resources, such as open HTTP connections, are cleaned up when the client
        is no longer in use.
        """
        pass
