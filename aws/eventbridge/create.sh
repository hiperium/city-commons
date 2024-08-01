#!/bin/bash
set -e

cd "$WORKING_DIR"/aws/eventbridge || {
    echo "Error moving to the 'EventBridge' directory."
    exit 1
}

echo ""
echo "CREATING SQS-DLQ FOR EVENTBRIDGE EVENT-BUS..."
aws sqs create-queue                                    \
    --queue-name "$DLQ_NAME"                            \
    --profile "$AWS_WORKLOADS_PROFILE" > /dev/null
echo "DONE!"

echo ""
echo "CREATING EVENTBRIDGE EVENT-BUS..."
aws events create-event-bus                             \
    --name "$EVENT_BUS_NAME"                            \
    --description "Event-Bus for the Hiperium City"     \
    --dead-letter-config Arn=arn:aws:sqs:"$AWS_WORKLOADS_REGION":"$AWS_WORKLOADS_ACCOUNT_ID":"$DLQ_NAME"    \
    --profile "$AWS_WORKLOADS_PROFILE"
echo "DONE!"

echo ""
echo "CREATING EVENTBRIDGE EVENT-BUS..."
sed -i'.bak' -e "s/AWS_REGION/$AWS_WORKLOADS_REGION/g; s/AWS_ACCOUNT_ID/$AWS_WORKLOADS_ACCOUNT_ID/g; s/QUEUE_NAME/$DLQ_NAME/g; s/EVENT_BUS_NAME/$EVENT_BUS_NAME/g" \
    "$WORKING_DIR"/aws/eventbridge/helper/set-dlq-attributes.json

aws sqs set-queue-attributes    \
    --queue-url https://sqs."$AWS_WORKLOADS_REGION".amazonaws.com/"$AWS_WORKLOADS_ACCOUNT_ID"/"$DLQ_NAME"   \
    --attributes file://"$WORKING_DIR"/aws/eventbridge/helper/set-dlq-attributes.json                       \
    --profile "$AWS_WORKLOADS_PROFILE"

rm -f "$WORKING_DIR"/aws/eventbridge/helper/set-dlq-attributes.json
mv "$WORKING_DIR"/aws/eventbridge/helper/set-dlq-attributes.json.bak "$WORKING_DIR"/aws/eventbridge/helper/set-dlq-attributes.json
