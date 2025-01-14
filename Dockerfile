FROM openjdk:21-slim

WORKDIR /app

COPY src ./src
COPY Configs ./Configs
COPY Log ./Log
COPY Results ./Results
COPY libs ./libs
COPY scl-star.jar ./scl-star.jar

CMD ["java", "-cp", "./libs/learnlib-distribution-0.16.0-dependencies-bundle.jar:./libs/opencsv-5.6.jar:./libs/slf4j-jdk14-1.7.36.jar:./libs/commons-cli-1.4.jar:scl-star.jar" , "main/Experiment"]
