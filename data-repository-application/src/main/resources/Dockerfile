FROM openjdk:17

CMD mvn clean install

COPY ./data-repository-application/target/data-repository-application.jar /data-repository-application.jar
ENTRYPOINT ["java","-jar","/data-repository-application.jar"]