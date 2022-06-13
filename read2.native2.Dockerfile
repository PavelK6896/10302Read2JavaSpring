ARG deployBuild="https://read2-v1.herokuapp.com"

FROM ghcr.io/graalvm/native-image:ol8-java17 AS builder
RUN microdnf install maven

RUN mvn -v
RUN native-image --version

FROM builder AS verify
ADD --chown=mvn:mvn pom.xml /home/app/pom.xml
WORKDIR /home/app
RUN mvn verify -P native -D skipTests -D maven.test.skip=true -e --fail-never

FROM node:16.15.1-alpine AS build-front
RUN apk add curl unzip zip

ARG token="-"
ARG appNameFolderGit="10317Read2TSAngular-master"

RUN curl -H "Authorization: token $token" -L -O https://github.com/PavelK6896/10317Read2TSAngular/archive/refs/heads/master.zip
RUN unzip -d ./source/ master.zip

WORKDIR /source/$appNameFolderGit/
RUN npm install
RUN npm run "build prod static"

ARG deployBuild
ARG appName="10317-read2-ts-angular"
ARG search="http://localhost:8080"
ARG files="./dist/$appName/*"

RUN for f in $files; do \
         if [[ $search != "" && $deployBuild != "" && "$f" == *"main"* ]]; then \
           sed -i "s+$search+$deployBuild+g" "$f"; \
         fi \
       done

WORKDIR /
RUN cp -r ./source/10317Read2TSAngular-master/dist/10317-read2-ts-angular/ mian

FROM verify AS maven
ADD --chown=mvn:mvn /src /home/app/src

RUN rm -r /home/app/src/main/resources/static/main
COPY --from=build-front "/mian" /home/app/src/main/resources/static/main

WORKDIR /home/app

RUN mvn clean package -P native -D skipTests -D maven.test.skip=true

FROM oraclelinux:8-slim
ARG deployBuild
ENV NAME_APP=read2-app
ENV HOST=$deployBuild

COPY --from=maven "/home/app/target/$NAME_APP" spring-boot-native
CMD [ "sh", "-c", "./spring-boot-native " ]


# docker build --build-arg deployBuild="http://localhost:8080" -t registry.heroku.com/read2-v1/web -f read2.native2.Dockerfile .
# docker build --build-arg deployBuild="https://read2-v1.herokuapp.com" -t registry.heroku.com/read2-v1/web -f read2.native2.Dockerfile .
# docker run --name read2-v1-c1 -p 8080:8080 -d registry.heroku.com/read2-v1/web


# heroku auth:token
# docker login --username=_ --password= registry.heroku.com
# docker push registry.heroku.com/read2-v1/web
# heroku container:release web --app read2-v1
# heroku log --app read2-v1

