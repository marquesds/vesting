FROM openjdk:11.0.11-jdk-slim AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew uberJar

FROM openjdk:11.0.11-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/ /app/

ENTRYPOINT ["java","-jar","/app/vesting-1.0-uber.jar"]
CMD ["file-path", "target-date", "precision"]
