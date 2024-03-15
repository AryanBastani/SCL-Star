# syntax=docker/dockerfile:1
FROM openjdk:17-oracle
WORKDIR /app

#RUN \
## Update
#apt-get update -y && \
## Install Java
#apt-get install default-jre -y

COPY src ./src 
COPY libs ./libs
COPY resources ./resources
COPY build ./build
COPY cl-star.jar ./cl-star.jar

CMD ["java", "-cp", "./libs/learnlib-distribution-0.16.0-dependencies-bundle.jar:./libs/opencsv-5.6.jar:./libs/slf4j-jdk14-1.7.36.jar:./libs/commons-cli-1.4.jar:cl-star.jar" , "Run_experiment"]
