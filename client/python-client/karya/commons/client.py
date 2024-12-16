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
    @abstractmethod
    async def create_user(self, request: CreateUserRequest) -> User:
        pass

    @abstractmethod
    async def submit_plan(self, request: SubmitPlanRequest) -> Plan:
        pass

    @abstractmethod
    async def get_plan(self, plan_id: uuid.UUID) -> GetPlanResponse:
        pass

    @abstractmethod
    async def update_plan(self, request: UpdatePlanRequest) -> Plan:
        pass

    @abstractmethod
    async def cancel_plan(self, plan_id: uuid.UUID) -> Plan:
        pass

    @abstractmethod
    async def get_summary(self, plan_id: uuid.UUID) -> GetSummaryResponse:
        pass

    @abstractmethod
    async def close(self) -> None:
        pass
