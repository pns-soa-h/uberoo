#!/bin/bash

mvn package
docker-compose build
docker build -t uberoo-scenario ./uberoo-scenario/docker