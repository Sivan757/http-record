FROM openjdk:11-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/http-record/ /app/
WORKDIR /app/bin
CMD ["./http-record"]
