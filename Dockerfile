FROM openjdk:8-jdk-alpine
LABEL maintainer=magabemarwa@gmail.com
VOLUME /tmp
EXPOSE 2000
ARG JAR_FILE=target/livechat-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} websocket-demo.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/websocket-demo.jar"]
