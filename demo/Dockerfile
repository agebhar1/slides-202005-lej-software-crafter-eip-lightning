FROM openjdk:8-jdk-alpine

COPY target/demo.jar /opt/app/demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/demo.jar"]