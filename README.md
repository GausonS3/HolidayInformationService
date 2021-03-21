# HolidayInformationService
Bluestone interview task

# Build jar file
mvn clean package

# Build docker image
docker build --tag=holiday-information:latest .

# Run docker image
docker run -e NAGER-API-ADDRESS='https://date.nager.at/Api/v2' -p 8080:8080 holiday-information:latest