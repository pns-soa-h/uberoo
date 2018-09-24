#!/bin/bash

echo "Connecting to Uberoo :"
curl localhost:8080

echo ""

echo "Checking Uberoo's health :"
curl localhost:8080/actuator/health

echo ""