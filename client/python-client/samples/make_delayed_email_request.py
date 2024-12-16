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
from karya.commons.entities.models.Action import RestApiRequest, EmailRequest
from karya.commons.entities.models.Plan import OneTime
from karya.clients.KaryaRestClient import KaryaRestClient


async def main():
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)

    email_action = EmailRequest(
        recipient="recipient@gmail.com",
        subject="Karya notification",
        message="Hello from Karya!",
    )

    plan_request = SubmitPlanRequest(
        user_id=user.id,
        description="Make a one-time email request python client with failure hook",
        period_time="PT5S",
        max_failure_retry=1,
        plan_type=OneTime(),
        action=email_action,
    )

    plan = await client.submit_plan(plan_request)
    print(plan)

if __name__ == "__main__":
    import asyncio
    asyncio.run(main())