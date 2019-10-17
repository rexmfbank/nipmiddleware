FROM java:openjdk-8-jre
VOLUME /tmp

ADD nipmiddleware.jar app.jar
EXPOSE 7222
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

