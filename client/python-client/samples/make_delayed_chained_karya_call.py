import sys
import os

# Add the root directory of the project to the Python path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from karya.commons.config import ClientConfig
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
)
from karya.commons.entities.models.Action import RestApiRequest, ChainedRequest
from karya.commons.entities.models.Plan import Recurring, OneTime
from karya.clients.KaryaRestClient import KaryaRestClient


async def main():
    """
    Main entry point for the Python client to interact with the Karya API.

    This function demonstrates how to:
    1. Create a new user using the Karya API.
    2. Submit a recurring plan with a chained API request.
    3. Submit a one-time plan that includes a chained API action.

    The function uses the `KaryaRestClient` to interact with the Karya API, sends requests,
    and prints the resulting user and plan details to the console.
    """
    # Initialize the KaryaRestClient with default development configuration (localhost:8080)
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    # Step 1: Create a new user
    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)  # Print the created user object

    # Step 2: Create a chained action (i.e., a series of API requests)
    chained_action = ChainedRequest(
        request=SubmitPlanRequest(
            user_id=user.id,  # Use the created user's ID
            description="Make a recurring API call from python client",  # Plan description
            period_time="PT5S",  # Time period between calls (5 seconds)
            max_failure_retry=3,  # Retry count in case of failure
            plan_type=Recurring(end_at=None),  # Recurring plan with no end time
            action=RestApiRequest(
                base_url="eox7wbcodh9parh.m.pipedream.net"
            ),  # API request action
        )
    )

    # Step 3: Create a plan that includes the chained action
    plan_request = SubmitPlanRequest(
        user_id=user.id,  # Use the created user's ID
        description="Make a recurring Chained call from python client",  # Plan description
        period_time="PT7S",  # Time period between calls (7 seconds)
        max_failure_retry=3,  # Retry count in case of failure
        plan_type=OneTime(),  # One-time plan
        action=chained_action,  # Chained action to be executed in the plan
    )

    # Step 4: Submit the plan and print the result
    plan = await client.submit_plan(plan_request)
    print(plan)  # Print the submitted plan details


# Ensure the script runs as the main program
if __name__ == "__main__":
    import asyncio

    # Run the main function asynchronously
    asyncio.run(main())
