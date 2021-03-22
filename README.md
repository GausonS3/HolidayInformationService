# HolidayInformationService
Service accepts two country codes and a date and return next holiday after the given date that will happen on the same 
day in both countries in the given or following year. 

# Build jar file
mvn clean install

# Build docker image
docker build --tag=holiday-information:latest .

# Run docker image
docker run -p 8080:8080 holiday-information:latest

configurable envs:

-e NAGER-API-ADDRESS=
-e SERVER-PORT=