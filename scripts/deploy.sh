#!/bin/bash

cd /home/ubuntu/app

DOCKER_APP_NAME=spring


# 실행중인 blue가 있는지
EXIST_BLUE=$(sudo docker-compose -p DOCKER_APP_NAME-blue -f docker-compose.blue.yml ps | grep Up)

# blue가 실행중이면 green up
if [ -z "$EXIST_BLUE" ]; then
	echo "green up"
  	sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

  	sleep 30

  	sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
  	sudo docker image prune -af

# green이 실행중이면 blue up
else
  echo "blue up"
  	sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

  	sleep 30

  	sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
  	sudo docker image prune -af # 사용하지 않는 이미지 삭제

fi