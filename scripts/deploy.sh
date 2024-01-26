#!/bin/bash

cd /home/ubuntu/app

DOCKER_APP_NAME=spring


# 실행중인 blue가 있는지
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep running)

# green이 실행중이면 blue up
if [ -z "$EXIST_BLUE" ]; then
	echo "blue up"
	docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

	sleep 30

	docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
	sudo docker image prune -af # 사용하지 않는 이미지 삭제

# blue가 실행중이면 green up
else
	echo "green up"
	docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

	sleep 30

	docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
	sudo docker image prune -af
fi