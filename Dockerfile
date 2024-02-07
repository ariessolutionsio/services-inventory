FROM openjdk:21-slim
WORKDIR /usr/app
ADD inventory_availability_api/target/ /usr/app
EXPOSE 8080
ENTRYPOINT ["java","-jar","inventory_availability_api-0.0.1-SNAPSHOT.jar"]
