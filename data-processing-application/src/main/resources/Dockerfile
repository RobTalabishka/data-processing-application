FROM openjdk:17

CMD mvn clean install

COPY ./data-processing-application/target/data-processing-application.jar /data-processing-application.jar
ENTRYPOINT ["java","-jar","/data-processing-application.jar"]