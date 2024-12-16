import sys
import os

# Add the root directory of the project to the Python path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from karya.commons.config import ClientConfig
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
)
from karya.commons.entities.models.Hook import Hook, Trigger
from karya.commons.entities.models.Action import RestApiRequest
from karya.commons.entities.models.Plan import OneTime
from karya.clients.KaryaRestClient import KaryaRestClient


async def main():
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    create_user_request = CreateUserRequest(
        name="python-client"
    )
    user = await client.create_user(create_user_request)
    print(user)

    failureHook = Hook(
        trigger=Trigger.ON_FAILURE,
        action=RestApiRequest(base_url="eox7wbcodh9parh.m.pipedream.net"),
    )

    planRequest = SubmitPlanRequest(
        user_id=user.id,
        description="Make a one-time API call from python client with failure hook",
        period_time="PT5S",
        max_failure_retry=1,
        plan_type=OneTime(),
        action=RestApiRequest(base_url="eox7wbcodh9parh.m.pipedream.net----"),
        hooks=[failureHook],
    )

    plan = await client.submit_plan(planRequest)
    print(plan)

    summary = await client.get_summary("3ac19214-4212-44f7-bf13-1f919607be90")
    print("\n")
    print(summary)


if __name__ == "__main__":
    import asyncio
    asyncio.run(main())