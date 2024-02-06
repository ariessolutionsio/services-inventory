FROM openjdk:21-slim
WORKDIR /usr/app
ADD inventory_availability_api/target/ /usr/app
EXPOSE 8080
ENTRYPOINT ["java","-jar","inventory-1.0.jar"]
