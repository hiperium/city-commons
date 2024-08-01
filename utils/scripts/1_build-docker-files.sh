#!/bin/bash
set -e

cd "$WORKING_DIR"/docker || {
    echo "Error moving to the 'docker' directory."
    exit 1
}

### ASK TO PRUNE DOCKER SYSTEM
"$WORKING_DIR"/utils/scripts/common/docker-system-prune.sh

echo ""
echo "NATIVE LINUX BUILDER IMAGE..."
echo ""
read -r -p "> What version do you want to use? [1.0.0] " native_image_version
if [ -z "$native_image_version" ]; then
    native_image_version="1.0.0"
fi

docker build -t native-image-builder:"$native_image_version" .

echo ""
read -r -p "> Do you want to publish the built version $native_image_version? [y/N] " publish_version
publish_version=$(echo "$publish_version" | tr '[:upper:]' '[:lower:]')
if [[ "$publish_version" =~ ^(yes|y)$ ]]; then
    docker login
    docker tag native-image-builder:"$native_image_version" hiperium/native-image-builder:"$native_image_version"
    docker push hiperium/native-image-builder:"$native_image_version"

    docker tag native-image-builder:"$native_image_version" hiperium/native-image-builder:latest
    docker push hiperium/native-image-builder:latest
fi
