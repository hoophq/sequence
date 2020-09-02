FROM clojure AS builder

COPY project.clj /usr/src/app/

WORKDIR /usr/src/app

RUN lein deps

COPY . /usr/src/app/

RUN lein uberjar

FROM openjdk:8-jdk-buster

COPY --from=builder /usr/src/app/target/decimals-0.0.1-standalone.jar /decimals/app.jar

EXPOSE 8910 

CMD ["java", "-jar", "/decimals/app.jar"]
