#!/bin/bash

mvn install
docker build -t cpm:latest .
docker save -o cpm.tar cpm:latest
scp -i mykey.pem -r cpm.tar ubuntu@18.176.7.247:/home/ubuntu