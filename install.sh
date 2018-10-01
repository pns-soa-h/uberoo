#!/bin/bash

mvn package
docker build -t uberoo-scenario ./uberoo-scenario/docker