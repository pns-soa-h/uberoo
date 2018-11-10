#!/bin/bash

mvn clean package -DskipTests=true
docker-compose build
docker build -t uberoo-scenario ./uberoo-scenario/docker
docker build -t uberoo-load ./uberoo-load/docker

