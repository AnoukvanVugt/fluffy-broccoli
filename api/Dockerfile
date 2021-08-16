FROM  openjdk:11

EXPOSE 8080

RUN mkdir /api
COPY ./build/libs/api.jar /api/api.jar
WORKDIR /api
CMD "java" "-jar" "api.jar"