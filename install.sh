#!/bin/bash

mvn clean package
docker-compose build
docker build -t uberoo-scenario ./uberoo-scenario/docker

