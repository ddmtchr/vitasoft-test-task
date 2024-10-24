FROM maven:3.9.9-eclipse-temurin-11 as build

WORKDIR /usr/src/app
COPY pom.xml .
COPY src ./src
ENV DATABASE_URL=jdbc:postgresql://postgres:5432/vitasoft-test-task
RUN mvn clean package
ENTRYPOINT ["java","-jar","target/vitasoft-test-task-0.0.1-SNAPSHOT.jar"]