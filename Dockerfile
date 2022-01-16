FROM alpine
EXPOSE 8080:8080
RUN mkdir /app
COPY ./http-record /app/
WORKDIR /app
ENTRYPOINT ["./http-record"]
