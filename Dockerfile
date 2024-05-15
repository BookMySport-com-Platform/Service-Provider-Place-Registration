FROM ubuntu:latest

RUN apt update

RUN apt install openjdk-17-jdk -y

RUN apt install maven -y

WORKDIR /service-provider

COPY . /service-provider/

ENTRYPOINT [ "mvn","spring-boot:run" ]

