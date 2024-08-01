#!/bin/bash
set -e

echo "[AWS]"
echo "AWS_WORKLOADS_ENV     = $AWS_WORKLOADS_ENV"
echo "AWS_WORKLOADS_PROFILE = $AWS_WORKLOADS_PROFILE"
echo "AWS_WORKLOADS_ACCOUNT = $AWS_WORKLOADS_ACCOUNT_ID"
echo "AWS_WORKLOADS_REGION  = $AWS_WORKLOADS_REGION"
echo ""
echo "[CUSTOM]"
echo "EVENT_BUS_NAME        = $EVENT_BUS_NAME"
echo "DLQ_NAME              = $DLQ_NAME"
echo ""

read -r -p 'Press [Enter] to continue...'
