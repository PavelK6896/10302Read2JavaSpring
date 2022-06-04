FROM ghcr.io/graalvm/native-image:ol8-java17 AS builder
RUN microdnf install maven

RUN mvn -v
RUN native-image --version

FROM builder AS verify
ADD --chown=mvn:mvn pom.xml /home/app/pom.xml
WORKDIR /home/app
RUN mvn verify -P native -D skipTests -D maven.test.skip=true -e --fail-never

FROM verify AS maven
ADD --chown=mvn:mvn /src /home/app/src
WORKDIR /home/app

RUN mvn clean package -P native -D skipTests -D maven.test.skip=true

FROM oraclelinux:8-slim
ENV NAME_APP=read2-app

COPY --from=maven "/home/app/target/$NAME_APP" spring-boot-native
CMD [ "sh", "-c", "./spring-boot-native " ]


# docker build -t read2-v1 -f read2.native.Dockerfile .
# docker run --name read2-v1-c1 -p 8080:8080 -d read2-v1
