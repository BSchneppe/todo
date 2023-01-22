# Service description
The service is a simple REST API that allows to create, read and update todos. 
It is written in Java 17 and uses Spring Boot 2.7.8.
The data is stored in an H2 in memory database.
# Assumptions
- The service is not meant to be used in production, but rather as a showcase of my skills.
- The service is not meant to be used by multiple users at the same time, not authentication is implemented.
- The nature of an in memory database is that it is lost when the service is stopped. 
  Therefore, the service is not meant to be used for long term storage of todos.
# Tech stack
- Java 17
- Spring Boot 2.7.8
- H2 in memory database
- JUnit 5
- Mockito
- Gradle
- Lombok
- MapStruct
- SpringDoc / OpenAPI 3
- Liquibase
- Zalando Problem
# How to
### Build the service
The service can be built using the gradle wrapper:
```
./gradlew build
```
### Run automatic tests
The service can be tested using the gradle wrapper:
```
./gradlew test
```
### Run the service locally
The service can be run locally using the gradle wrapper:
```
./gradlew bootRun
```
The service can then be accessed at http://localhost:8080
To access the OpenAPI documentation, go to http://localhost:8081/actuator/swagger-ui

### Dockerize the service
The service can be dockerized using the gradle wrapper:
```
./gradlew bootBuildImage --imageName=todos
```
The service can then be run using docker:
```bash
docker run -p 8080:8080 -p 8081:8081 -t todos
```
The service can then be accessed at http://localhost:8080
To access the OpenAPI documentation, go to http://localhost:8081/actuator/swagger-ui

the attached docker-compose.yml file can be used to run the service in a docker container.
```bash
docker-compose up
```
