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
    """
    Main entry point for the Python client to interact with the Karya API.

    This function demonstrates how to:
    1. Create a new user using the Karya API.
    2. Submit a recurring plan that makes a REST API call.
    3. Print the resulting user and plan details to the console.

    The function uses the `KaryaRestClient` to interact with the API and includes:
    - Creating a user
    - Defining and submitting a recurring plan with a REST API request as the action
    - Handling the submission and displaying the results
    """

    # Step 1: Initialize the KaryaRestClient with the default development configuration (localhost:8080)
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    # Step 2: Create a new user with the name "python-client"
    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)  # Print the created user object to the console

    # Step 3: Define a REST API request action to be executed as part of the plan
    rest_action = RestApiRequest(
        protocol=Protocol.HTTPS,  # Use HTTPS for secure communication
        base_url="eox7wbcodh9parh.m.pipedream.net",  # Base URL for the REST API
        method=Method.POST,  # HTTP method for the request (POST)
        headers={"content-type": "application/json"},  # Set the content type to JSON
        body=JsonBody.from_dict(
            {"message": "Hello from python client"}
        ),  # JSON body to send in the request
        timeout=2000,  # Timeout for the request (in milliseconds)
    )

    # Step 4: Define a recurring plan that uses the REST API request action
    plan_request = SubmitPlanRequest(
        user_id=user.id,  # Use the created user's ID
        description="Make a recurring API call from python client",  # Description of the plan
        period_time="PT7S",  # Time period between each execution (7 seconds)
        max_failure_retry=3,  # Retry count in case of failure
        plan_type=Recurring(  # Define a recurring plan
            end_at=None,  # No specific end time, so it will continue indefinitely
        ),
        action=rest_action,  # The action to be executed as part of the plan (REST API call)
    )

    # Step 5: Submit the plan to the API and print the resulting plan details
    plan = await client.submit_plan(plan_request)
    print(plan)  # Print the submitted plan object to the console


# Ensure the script runs as the main program
if __name__ == "__main__":
    import asyncio

    # Run the main function asynchronously
    asyncio.run(main())
