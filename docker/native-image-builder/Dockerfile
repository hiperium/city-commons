### Amazon Linux versions: https://gallery.ecr.aws/amazonlinux/amazonlinux
FROM --platform=linux/arm64 public.ecr.aws/amazonlinux/amazonlinux:2

### Amazon Linux installing dependencies
RUN yum -y update                                                              && \
    yum install -y zip unzip tar gzip bzip2-devel ed gcc gcc-c++ gcc-gfortran     \
        less libcurl-devel openssl openssl-devel readline-devel xz-devel          \
        zlib-devel glibc-static libcxx libcxx-devel llvm-toolset-7 zlib-static && \
    rm -rf /var/cache/yum

### GraalVM for OpenJDK - Verity LTS version at: https://github.com/graalvm/graalvm-ce-builds/releases
ENV JAVA_VERSION 21.0.2
ENV GRAALVM_FILENAME graalvm-community-jdk-${JAVA_VERSION}_linux-aarch64_bin.tar.gz
RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-${JAVA_VERSION}/${GRAALVM_FILENAME} | tar -xvz
RUN mv graalvm-community-openjdk-${JAVA_VERSION}+13.1 /usr/lib/graalvm

### Maven - Verify latest version at: https://dlcdn.apache.org/maven/maven-3/
ENV MVN_VERSION 3.9.8
ENV MVN_FOLDERNAME apache-maven-${MVN_VERSION}
ENV MVN_FILENAME apache-maven-${MVN_VERSION}-bin.tar.gz
RUN curl -4 -L https://dlcdn.apache.org/maven/maven-3/${MVN_VERSION}/binaries/${MVN_FILENAME} | tar -xvz
RUN mv "$MVN_FOLDERNAME" /usr/lib/maven

### Required Links
RUN ln -s /usr/lib/maven/bin/mvn              /usr/bin/mvn
RUN ln -s /usr/lib/graalvm/bin/jar            /usr/bin/jar
RUN ln -s /usr/lib/graalvm/bin/java           /usr/bin/java
RUN ln -s /usr/lib/graalvm/bin/native-image   /usr/bin/native-image

### Lambda Builders
RUN amazon-linux-extras enable python3.8
RUN yum clean metadata && yum -y install python3.8
RUN curl -L get-pip.io | python3.8
RUN pip3 install aws-lambda-builders

### Environment Variables
ENV JAVA_HOME /usr/lib/graalvm
ENV MAVEN_HOME /usr/lib/maven

WORKDIR /workspace
ENTRYPOINT ["sh"]
