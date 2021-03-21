FROM openjdk:15-jdk-alpine
ARG JAR_FILE=target/HolidayInformationService-1.0.jar
WORKDIR /opt/app
COPY ${JAR_FILE} holiday-information-service.jar
ENTRYPOINT ["java","-jar","holiday-information-service.jar"]