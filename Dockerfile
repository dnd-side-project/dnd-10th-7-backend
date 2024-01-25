FROM openjdk:17-jdk
ENV APP_HOME=/home/ubuntu/chillin-Server
WORKDIR $APP_HOME
COPY sendback-0.0.1-SNAPSHOT.jar chillin-server.jar
ENTRYPOINT ["java","-jar", "chillin-server.jar"]