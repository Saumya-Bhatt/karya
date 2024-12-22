FROM gradle:8.7-jdk11 AS build

LABEL maintainer="saumya.bhatt106@gmail.com"

COPY . /home/app
WORKDIR /home/app

RUN ./gradlew clean servers-scheduler:shadowJar --no-daemon

FROM eclipse-temurin:11-jre-jammy

COPY --from=build /home/app/servers/scheduler/deploy/run.sh ./run.sh
COPY --from=build /home/app/servers/scheduler/build/libs/io.github.saumya-bhatt-scheduler-*-all.jar ./app.jar

ENTRYPOINT ["bash", "run.sh"]
