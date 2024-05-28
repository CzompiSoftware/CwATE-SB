# First stage: build the application
FROM gradle:8.7.0-jdk21-alpine AS build
COPY . /work
WORKDIR /work
RUN ./gradlew build

# Second stage: create a slim image
FROM eclipse-temurin:21-jre-alpine

VOLUME [ "/data" ]
EXPOSE 80
ENV \
    # CwATE-SB version
    CWATE_VERSION=1.0.0+build.2


WORKDIR /app

COPY --from=build /work/build/libs/CwATE-SB-$CWATE_VERSION.jar /app/CwATE-SB.jar

HEALTHCHECK CMD curl --fail http://localhost || exit 1 # test if it is working. Source: https://www.docker.com/blog/9-tips-for-containerizing-your-net-application/

ENTRYPOINT ["java", "-jar", "/app/CwATE-SB.jar"]
