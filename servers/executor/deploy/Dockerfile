FROM gradle:8.7-jdk17 AS build

LABEL maintainer="saumya.bhatt106@gmail.com"

COPY . /home/app
WORKDIR /home/app

RUN ./gradlew clean servers-executor:shadowJar --no-daemon

FROM eclipse-temurin:17-jre

COPY --from=build /home/app/servers/executor/deploy/run.sh ./run.sh
COPY --from=build /home/app/servers/executor/build/libs/io.github.saumya-bhatt-executor-*-all.jar ./app.jar

ENTRYPOINT ["bash", "run.sh"]
