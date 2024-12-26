### Amazon Linux versions: https://gallery.ecr.aws/amazonlinux/amazonlinux
FROM --platform=linux/arm64 public.ecr.aws/amazonlinux/amazonlinux:2023

### Installing required dependencies
RUN yum -y update && \
    yum install -y tar unzip gzip bzip2-devel zlib-devel gcc gcc-c++ && \
    rm -rf /var/cache/yum

### GraalVM for OpenJDK - Verity LTS version at: https://github.com/graalvm/graalvm-ce-builds/releases
ARG JAVA_VERSION=21.0.2
ENV JAVA_HOME /usr/lib/graalvm
ARG GRAALVM_FILENAME=graalvm-community-jdk-${JAVA_VERSION}_linux-aarch64_bin.tar.gz
RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-${JAVA_VERSION}/${GRAALVM_FILENAME} | tar -xvz && \
    mv graalvm-community-openjdk-${JAVA_VERSION}+13.1 ${JAVA_HOME}

### Maven - Verify latest version at: https://dlcdn.apache.org/maven/maven-3/
ARG MAVEN_VERSION=3.9.8
ENV MAVEN_HOME /usr/lib/maven
ARG MAVEN_FILENAME=apache-maven-${MAVEN_VERSION}-bin.tar.gz
RUN curl -4 -L https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/${MAVEN_FILENAME} | tar -xvz && \
    mv apache-maven-${MAVEN_VERSION} ${MAVEN_HOME}

### Required Links
RUN ln -s ${JAVA_HOME}/bin/jar       /usr/bin/jar          && \
    ln -s ${JAVA_HOME}/bin/java      /usr/bin/java         && \
    ln -s ${JAVA_HOME}/native-image  /usr/bin/native-image && \
    ln -s ${MAVEN_HOME}/bin/mvn      /usr/bin/mvn
