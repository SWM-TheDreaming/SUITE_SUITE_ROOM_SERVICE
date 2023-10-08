FROM openjdk:11-jre-slim
ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} suite-room.jar
ENTRYPOINT ["java","-jar","/suite-room.jar"]