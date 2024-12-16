from karya.commons.client import Client
from karya.commons.config import ClientConfig
from karya.commons.entities.models.User import User
from karya.commons.entities.models.Plan import Plan
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
    UpdatePlanRequest,
)
from karya.commons.entities.response import (
    GetPlanResponse,
    GetSummaryResponse,
)
import httpx
import dataclasses

class KaryaRestClient(Client):

    api_version = 'v1'
    plans_endpoint = f'{api_version}/plan'
    users_endpoint = f'{api_version}/user'
    
    def __init__(self, config: ClientConfig):
        super().__init__()
        self.config = config
        self.client = httpx.AsyncClient()

    async def create_user(self, request: CreateUserRequest) -> User:
        url = f'{self.config.get_base_url()}/{self.users_endpoint}'
        response = await self.client.post(url, json=dataclasses.asdict(request))
        response.raise_for_status()
        return User(**response.json())
    
    async def submit_plan(self, request: SubmitPlanRequest) -> Plan:
        url = f'{self.config.get_base_url()}/{self.plans_endpoint}'
        response = await self.client.post(url, json=dataclasses.asdict(request))
        response.raise_for_status()
        return Plan(**response.json())
    
    async def get_plan(self, plan_id: str) -> GetPlanResponse:
        url = f'{self.config.get_base_url()}/{self.plans_endpoint}/{plan_id}'
        response = await self.client.get(url)
        response.raise_for_status()
        return GetPlanResponse(**response.json())
    
    async def update_plan(self, request: UpdatePlanRequest) -> Plan:
        url = f'{self.config.get_base_url()}/{self.plans_endpoint}/{request.id}'
        response = await self.client.patch(url, json=dataclasses.asdict(request))
        response.raise_for_status()
        return Plan(**response.json())
    
    async def cancel_plan(self, plan_id: str) -> Plan:
        url = f'{self.config.get_base_url()}/{self.plans_endpoint}/{plan_id}'
        response = await self.client.post(url)
        response.raise_for_status()
        return Plan(**response.json())
    
    async def get_summary(self, plan_id: str) -> GetSummaryResponse:
        url = f'{self.config.get_base_url()}/{self.plans_endpoint}/{plan_id}/summary'
        response = await self.client.get(url)
        response.raise_for_status()
        return GetSummaryResponse(**response.json())
    
    async def close(self) -> None:
        await self.client.aclose()