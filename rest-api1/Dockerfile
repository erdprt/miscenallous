FROM openjdk:8-jdk-alpine

#RUN apk update && apk add bash && apk add dnsmasq && apk add curl && apk add openrc && apk add dig
#RUN echo "server=/consul/10.98.211.58#53001" > /etc/dnsmasq.d/10-consul

RUN apk update && apk add bash && apk add dnsmasq && apk add curl && apk add bind-tools
COPY dig.sh /
RUN chmod +x /dig.sh


LABEL application.name="rest-api1" \
      application.desc="rest api" \
      service.8080_tcp.name="rest-api1" \
      service.8080_tcp.desc="rest-api1"

VOLUME /tmp
COPY target/rest-api1-1.0.0.jar app.jar

#EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]