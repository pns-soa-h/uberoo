#!/bin/bash

mvn clean package -DskipTests=true
docker-compose build
docker build -t uberoo-scenario ./uberoo-scenario/docker

