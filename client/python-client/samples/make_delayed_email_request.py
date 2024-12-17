import sys
import os

# Add the root directory of the project to the Python path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from karya.commons.config import ClientConfig
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
)
from karya.commons.entities.models.Action import EmailRequest
from karya.commons.entities.models.Plan import OneTime
from karya.clients.KaryaRestClient import KaryaRestClient


async def main():
    """
    Main entry point for the Python client to interact with the Karya API.

    This function demonstrates how to:
    1. Create a new user using the Karya API.
    2. Submit a one-time plan that sends an email request.
    3. Print the resulting user and plan details to the console.

    The function uses the `KaryaRestClient` to interact with the API and handles both
    user creation and email action submission.
    """
    # Initialize the KaryaRestClient with the default development configuration (localhost:8080)
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    # Step 1: Create a new user
    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)  # Print the created user object to the console

    # Step 2: Create an email action to be included in the plan
    email_action = EmailRequest(
        recipient="recipient@gmail.com",  # Email recipient
        subject="Karya notification",  # Email subject
        message="Hello from Karya!",  # Email message body
    )

    # Step 3: Define a one-time plan with the email action
    plan_request = SubmitPlanRequest(
        user_id=user.id,  # Use the created user's ID
        description="Make a one-time email request python client with failure hook",  # Plan description
        period_time="PT5S",  # Time between retries (if any) in ISO 8601 format (5 seconds)
        max_failure_retry=1,  # Max retry count in case of failure
        plan_type=OneTime(),  # Define a one-time plan (not recurring)
        action=email_action,  # The email action to be executed as part of the plan
    )

    # Step 4: Submit the plan to the API and print the resulting plan object
    plan = await client.submit_plan(plan_request)
    print(plan)  # Print the submitted plan details to the console


# Ensure the script runs as the main program
if __name__ == "__main__":
    import asyncio

    # Run the main function asynchronously
    asyncio.run(main())
