package com.twitter.kafka.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class KafkaDataProducer {

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessage(String message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(topic, UUID.randomUUID().toString(), message);
    }
}
