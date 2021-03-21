FROM openjdk:15-jdk-alpine
ARG JAR_FILE=target/HolidayInformationService-1.0.jar
ARG server_port_default=8080
ENV SERVER-PORT=$server_port_default
ARG nager_api_address_default='https://date.nager.at/Api/v2'
ENV NAGER-API-ADDRESS=$nager_api_address_default
WORKDIR /opt/app
COPY ${JAR_FILE} holiday-information-service.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","holiday-information-service.jar"]