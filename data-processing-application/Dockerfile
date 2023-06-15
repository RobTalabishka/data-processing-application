# Install maven and copy project for compilation
FROM maven:latest as MAVEN_TOOL_CHAIN

COPY src ./data-processing-application/src
COPY pom.xml ./data-processing-application/pom.xml

RUN mvn clean install

FROM openjdk:17

CMD mvn clean install

COPY ./data-processing-application/target/data-processing-application.jar /data-processing-application.jar
ENTRYPOINT ["java","-jar","/data-processing-application.jar"]