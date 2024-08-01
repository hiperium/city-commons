#!/bin/bash
set -e

cd "$WORKING_DIR"/aws/eventbridge || {
    echo "Error moving to the 'EventBridge' directory."
    exit 1
}

echo ""
echo "DELETING EVENTBRIDGE EVENT-BUS..."
aws delete-event-bus                        \
    --name "$EVENT_BUS_NAME"                \
    --profile "$AWS_WORKLOADS_PROFILE"

echo ""
echo "DELETING EVENTBRIDGE EVENT-BUS DLQ..."
aws sqs delete-queue                        \
    --queue-url "https://sqs.$AWS_WORKLOADS_REGION.amazonaws.com/$AWS_WORKLOADS_ACCOUNT_ID/$DLQ_NAME" \
    --profile "$AWS_WORKLOADS_PROFILE"
