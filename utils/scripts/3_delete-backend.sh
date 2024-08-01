#!/bin/bash
set -e

cd "$WORKING_DIR" || {
    echo "Error moving to the project's root directory."
    exit 1
}

echo ""
echo "AWS INFORMATION:"
echo ""
echo "- Workloads Environment: $AWS_WORKLOADS_ENV"
echo "- Workloads Profile    : $AWS_WORKLOADS_PROFILE"
echo "- Workloads Account    : $AWS_WORKLOADS_ACCOUNT_ID"
echo "- Workloads Region     : $AWS_WORKLOADS_REGION"

### DELETE EVENTBRIDGE
"$WORKING_DIR"/aws/eventbridge/delete.sh
