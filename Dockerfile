FROM openjdk:11
EXPOSE 8081
ADD target/kafka-tweet-stream-producer.jar kafka-tweet-stream-producer.jar
ENTRYPOINT ["java", "-jar", "kafka-tweet-stream-producer.jar"]