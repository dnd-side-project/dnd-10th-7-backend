FROM openjdk:17-jdk
ENV APP_HOME=/home/ubuntu/Sendback-Server
WORKDIR $APP_HOME
COPY build/libs/*.jar sendback-server.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","sendback-server.jar"]