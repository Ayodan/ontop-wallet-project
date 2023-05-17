FROM openjdk:17-jdk-slim-buster
RUN apt-get update && apt-get install -y curl
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/ontop.jar
ADD ${JAR_FILE} ontop.jar
ENTRYPOINT ["java","-jar","ontop.jar"]