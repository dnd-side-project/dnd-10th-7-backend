FROM openjdk:17-jdk
ENV APP_HOME=/home/ubuntu/app
WORKDIR $APP_HOME
COPY sendback-server.jar sendback-server.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","sendback-server.jar"]