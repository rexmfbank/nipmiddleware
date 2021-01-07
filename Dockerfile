FROM 068278763965.dkr.ecr.eu-west-1.amazonaws.com/open-jdk-8:latest
VOLUME /tmp

ADD nipmiddleware.jar app.jar
EXPOSE 7222
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

