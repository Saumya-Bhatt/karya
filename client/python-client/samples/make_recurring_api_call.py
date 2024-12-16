import sys
import os

# Add the root directory of the project to the Python path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from karya.commons.config import ClientConfig
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
)
from karya.commons.entities.models.Action import RestApiRequest
from karya.commons.entities.models.HttpBody import JsonBody
from karya.commons.entities.models.Plan import Recurring
from karya.clients.KaryaRestClient import KaryaRestClient
from karya.commons.entities.enums import Protocol, Method

async def main():

    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    create_user_request = CreateUserRequest(
        name="python-client"
    )
    user = await client.create_user(create_user_request)
    print(user)
    
    rest_action = RestApiRequest(
        protocol=Protocol.HTTPS,
        base_url="eox7wbcodh9parh.m.pipedream.net",
        method=Method.POST,
        headers={"content-type": "application/json"},
        body=JsonBody.from_dict({"message": "Hello from python client"}),
        timeout=2000
    )

    plan_request = SubmitPlanRequest(
        user_id=user.id,
        description="Make a recurring API call from python client",
        period_time="PT7S",
        max_failure_retry=3,
        plan_type=Recurring(
            end_at=None,
        ),
        action=rest_action,
    )

    plan = await client.submit_plan(plan_request)
    print(plan)

if __name__ == "__main__":
    import asyncio
    asyncio.run(main())