import sys
import os

# Add the root directory of the project to the Python path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from karya.commons.config import ClientConfig
from karya.commons.entities.requests import (
    CreateUserRequest,
    SubmitPlanRequest,
)
from karya.commons.entities.models.Action import SlackMessageRequest
from karya.commons.entities.models.Plan import Recurring
from karya.clients.KaryaRestClient import KaryaRestClient


async def main():
    """
    Main function to interact with the Karya API by:
    1. Creating a user.
    2. Creating a recurring plan that sends a Slack message every 7 seconds.
    3. Submitting the plan to the API and printing the plan details.

    This function demonstrates how to:
    - Create a user.
    - Define and submit a recurring plan that sends a message to a Slack channel.
    """

    # Step 1: Initialize the KaryaRestClient with the development configuration (localhost:8080)
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    # Step 2: Create a user named "python-client"
    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)  # Print the created user details

    # Step 3: Define the Slack message in JSON format
    slack_message = """[
        {
            "type": "section",
            "text": {
                "type": "mrkdwn",
                "text": "Hello, this is periodic slack message from Karya!"
            }
        }
    ]"""
    # This is the Slack message payload in Slack Block Kit JSON format.

    # Step 4: Create the Slack message action
    slack_action = SlackMessageRequest(
        channel="C083L324V99",  # The Slack channel to send the message to
        message=slack_message,  # The message content in JSON format
    )

    # Step 5: Define a recurring plan that sends the Slack message every 7 seconds
    plan_request = SubmitPlanRequest(
        user_id=user.id,  # User ID that will be associated with the plan
        description="Make a recurring API call from python client",  # Description of the plan
        period_time="PT7S",  # Duration between each execution (7 seconds)
        max_failure_retry=3,  # Retry count in case of failure
        plan_type=Recurring(  # Define a recurring plan
            end_at=None,  # No end time, the plan will run indefinitely
        ),
        action=slack_action,  # The action to execute: sending the Slack message
    )

    # Step 6: Submit the plan and print the result
    plan = await client.submit_plan(plan_request)
    print(plan)  # Print the submitted plan details to the console


# Ensure the script runs only if it's executed directly (not imported as a module)
if __name__ == "__main__":
    import asyncio

    # Run the main function asynchronously
    asyncio.run(main())
