FROM public.ecr.aws/lambda/provided:al2023-arm64

#### Installing required dependencies
RUN dnf update -y                && \
    dnf install -y wget tar gzip && \
    dnf clean all

### GraalVM for OpenJDK - Verity LTS version at: https://github.com/graalvm/graalvm-ce-builds/releases
ARG JAVA_VERSION=21.0.2
ENV JAVA_HOME /usr/lib/graalvm
ARG GRAALVM_FILENAME=graalvm-community-jdk-${JAVA_VERSION}_linux-aarch64_bin.tar.gz
RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-${JAVA_VERSION}/${GRAALVM_FILENAME} | tar -xvz && \
    mv graalvm-community-openjdk-${JAVA_VERSION}+13.1 ${JAVA_HOME}

### Required Links
RUN ln -s ${JAVA_HOME}/bin/jar       /usr/bin/jar          && \
    ln -s ${JAVA_HOME}/bin/java      /usr/bin/java         && \
    ln -s ${JAVA_HOME}/native-image  /usr/bin/native-image
