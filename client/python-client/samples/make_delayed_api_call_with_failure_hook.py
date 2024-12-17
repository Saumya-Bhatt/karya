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
    """
    Main entry point for the Python client to interact with the Karya API.

    This function demonstrates how to use the Karya API client to:
    1. Create a new user.
    2. Submit a plan with a failure hook.
    3. Retrieve a plan's summary.

    The function uses the `KaryaRestClient` to interact with the API, sends requests,
    and prints the resulting user, plan, and summary data to the console.
    """
    # Initialize the KaryaRestClient with default development configuration
    config = ClientConfig.dev()  # Uses a development configuration (localhost:8080)
    client = KaryaRestClient(config)

    # Step 1: Create a new user
    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)  # Print the created user object

    # Step 2: Define a failure hook
    failure_hook = Hook(
        trigger=Trigger.ON_FAILURE,  # Trigger hook on failure
        action=RestApiRequest(
            base_url="eox7wbcodh9parh.m.pipedream.net"
        ),  # Action on failure
    )

    # Step 3: Create a plan with the failure hook attached
    plan_request = SubmitPlanRequest(
        user_id=user.id,  # Use the created user's ID
        description="Make a one-time API call from python client with failure hook",  # Plan description
        period_time="PT5S",  # Period time for the plan (5 seconds)
        max_failure_retry=1,  # Maximum retry count on failure
        plan_type=OneTime(),  # Plan type: One-time execution
        action=RestApiRequest(
            base_url="eox7wbcodh9parh.m.pipedream.net----"
        ),  # API request to be made
        hooks=[failure_hook],  # Attach the failure hook
    )

    # Step 4: Submit the plan and print the result
    plan = await client.submit_plan(plan_request)
    print(plan)  # Print the submitted plan details

    # Step 5: Retrieve and print the summary of a specific plan by ID
    summary = await client.get_summary("3ac19214-4212-44f7-bf13-1f919607be90")
    print("\n")  # Print a new line for readability
    print(summary)  # Print the summary of the plan


# Ensure that the script runs as the main program
if __name__ == "__main__":
    import asyncio

    # Run the main function asynchronously
    asyncio.run(main())
