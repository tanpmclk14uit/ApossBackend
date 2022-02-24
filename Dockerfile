FROM openjdk:11
 
VOLUME /tmp
 
ADD build/ApossBackend-0.0.1-SNAPSHOT.jar app.jar
 
ENTRYPOINT exec java -jar app.jar
