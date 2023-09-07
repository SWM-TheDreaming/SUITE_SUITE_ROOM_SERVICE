FROM openjdk:11
ARG JAR_FILE=./build/libs/*.jar
VOLUME /allso_Img
COPY ${JAR_FILE} suite-room.jar
ENTRYPOINT ["java","-jar","/suite-room.jar"]