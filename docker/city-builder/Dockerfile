### Amazon Linux versions: https://gallery.ecr.aws/amazonlinux/amazonlinux
FROM --platform=linux/arm64 public.ecr.aws/amazonlinux/amazonlinux:2023

### Amazon Linux installing dependencies
RUN yum -y update                                                         && \
    yum install -y tar unzip gzip bzip2-devel ed gcc gcc-c++ gcc-gfortran    \
        less libcurl-devel openssl openssl-devel readline-devel xz-devel     \
        zlib-devel glibc-static zlib-static                               && \
    rm -rf /var/cache/yum

### GraalVM for OpenJDK - Verity LTS version at: https://github.com/graalvm/graalvm-ce-builds/releases
ARG JAVA_VERSION=21.0.2
ARG GRAALVM_FILENAME=graalvm-community-jdk-${JAVA_VERSION}_linux-aarch64_bin.tar.gz
RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-${JAVA_VERSION}/${GRAALVM_FILENAME} | tar -xvz && \
    mv graalvm-community-openjdk-${JAVA_VERSION}+13.1 /usr/lib/graalvm

### Maven - Verify latest version at: https://dlcdn.apache.org/maven/maven-3/
ARG MVN_VERSION=3.9.8
ARG USER_HOME_DIR="/root"
ARG MVN_FOLDERNAME=apache-maven-${MVN_VERSION}
ARG MVN_FILENAME=apache-maven-${MVN_VERSION}-bin.tar.gz
RUN curl -4 -L https://dlcdn.apache.org/maven/maven-3/${MVN_VERSION}/binaries/${MVN_FILENAME} | tar -xvz && \
    mv "$MVN_FOLDERNAME" /usr/lib/maven

### Required Links
RUN ln -s /usr/lib/maven/bin/mvn              /usr/bin/mvn
RUN ln -s /usr/lib/graalvm/bin/jar            /usr/bin/jar
RUN ln -s /usr/lib/graalvm/bin/java           /usr/bin/java
RUN ln -s /usr/lib/graalvm/bin/native-image   /usr/bin/native-image

### Environment Variables
ENV JAVA_HOME       /usr/lib/graalvm
ENV MAVEN_HOME      /usr/lib/maven

WORKDIR /workspace
ENTRYPOINT ["sh"]
