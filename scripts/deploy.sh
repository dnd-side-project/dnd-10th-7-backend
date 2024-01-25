#!/bin/bash
PROJECT_NAME=chillin

echo "> 현재 실행 중인 Docker 컨테이너 pid 확인" >> /home/ubuntu/deploy.log
CURRENT_PID=$(sudo docker container ls -q)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." >> /home/ubuntu/deploy.log
else
  echo "> sudo docker stop $CURRENT_PID"   # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  sudo docker stop $CURRENT_PID
  sleep 5
fi

cd /home/ubuntu/app
sudo docker build -t chillin-docker .
sudo docker run -d -p 8080:8080 chillin-docker