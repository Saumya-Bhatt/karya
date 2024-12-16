import sys
import os

# Add the root directory of the project to the Python path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from karya.commons.config import ClientConfig
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
)
from karya.commons.entities.models.Action import KafkaProducerRequest
from karya.commons.entities.models.Plan import Recurring
from karya.clients.KaryaRestClient import KaryaRestClient


async def main():
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)

    kafka_action = KafkaProducerRequest(
        topic="karya-test",
        message="Published from executor",
    )

    plan_request = SubmitPlanRequest(
        user_id=user.id,
        description="Make a recurring API call from python client",
        period_time="PT7S",
        max_failure_retry=3,
        plan_type=Recurring(
            end_at=None,
        ),
        action=kafka_action,
    )

    plan = await client.submit_plan(plan_request)
    print(plan)


if __name__ == "__main__":
    import asyncio

    asyncio.run(main())