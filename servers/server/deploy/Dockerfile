FROM gradle:8.7-jdk11 as build

LABEL maintainer="saumya.bhatt106@gmail.com"

COPY . /home/app
WORKDIR /home/app

RUN ./gradlew clean servers-server:shadowJar \
    --no-daemon \
    -Dorg.gradle.jvmargs="-Xmx2048m -XX:MaxMetaspaceSize=512m"

FROM openjdk:11.0.11-jre-slim

COPY --from=build \
    /home/app/servers/server/build/libs/karya-server-*.jar ./app.jar

ENTRYPOINT ["bash", "run.sh"]
