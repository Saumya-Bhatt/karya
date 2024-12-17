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
    """
    Main function to demonstrate how to interact with the Karya API by:
    1. Creating a user through the Karya API.
    2. Creating a recurring plan to send a Kafka message.
    3. Submitting the plan and printing the plan details.

    This function leverages the `KaryaRestClient` to:
    - Create a user.
    - Define and submit a recurring plan that sends a message to a Kafka topic.
    """

    # Step 1: Initialize the KaryaRestClient with default development configuration (localhost:8080)
    config = ClientConfig.dev()
    client = KaryaRestClient(config)

    # Step 2: Create a user with the name "python-client"
    create_user_request = CreateUserRequest(name="python-client")
    user = await client.create_user(create_user_request)
    print(user)  # Print the created user details to the console

    # Step 3: Define a KafkaProducerRequest to send a message to a Kafka topic
    kafka_action = KafkaProducerRequest(
        topic="karya-test",  # Kafka topic to send the message to
        message="Published from executor",  # Message to be sent to Kafka
    )

    # Step 4: Define a recurring plan that will send the Kafka message every 7 seconds
    plan_request = SubmitPlanRequest(
        user_id=user.id,  # The ID of the user for whom the plan is created
        description="Make a recurring API call from python client",  # Description of the plan
        period_time="PT7S",  # Time period between executions (7 seconds)
        max_failure_retry=3,  # Retry count in case of failure
        plan_type=Recurring(  # Define a recurring plan (it will keep repeating)
            end_at=None,  # No end time, so it will repeat indefinitely
        ),
        action=kafka_action,  # The action to execute: sending a Kafka message
    )

    # Step 5: Submit the plan and print the plan details
    plan = await client.submit_plan(plan_request)
    print(plan)  # Print the submitted plan object to the console


# Ensure the script runs as the main program
if __name__ == "__main__":
    import asyncio

    # Run the main function asynchronously
    asyncio.run(main())
