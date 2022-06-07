FROM maven:3.8.5-jdk-11-slim AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests

FROM openjdk:11.0.15-jre
RUN mkdir /app
COPY --from=build /project/target/filmwebscraper-1.0.0.jar /app/filmwebscraper-1.0.0.jar
WORKDIR /app
CMD "java" "-jar" "filmwebscraper-1.0.0.jar"