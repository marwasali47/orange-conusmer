FROM dockerproxy-iva.si.francetelecom.fr/openjdk:8-slim

ARG JAR_FILE

ENV ACTIVE_PROFILE ""

ADD ${JAR_FILE} /opt/hubme/orange-consumer-service.jar

WORKDIR /opt/hubme

RUN chown -R 1003540000:1003540000 /opt/hubme

USER 1003540000

ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=${ACTIVE_PROFILE} \
                            -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
                            -jar orange-consumer-service.jar"]
