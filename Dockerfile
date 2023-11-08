FROM openjdk:17-alpine

COPY build/libs/*.jar pet.jar


ENTRYPOINT ["java","-jar","pet.jar"]