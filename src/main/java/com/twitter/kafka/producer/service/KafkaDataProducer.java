package com.twitter.kafka.producer.service;

public interface KafkaDataProducer {

    void sendMessage(String message);
}
