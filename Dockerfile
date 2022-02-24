FROM openjdk:11
 
VOLUME /tmp
 
ADD build/target/ApossBackend-0.0.1-SNAPSHOT.jar app.jar
 
ENTRYPOINT exec java -jar app.jar
