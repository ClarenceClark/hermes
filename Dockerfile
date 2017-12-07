FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/hermes.jar /hermes/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/hermes/app.jar"]
