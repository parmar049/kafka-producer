package com.twitter.kafka.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Service
@Slf4j
public class KafkaDataProducerImpl implements KafkaDataProducer {

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(topic, UUID.randomUUID().toString(), message)
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send data to Kafka .", ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.debug("Sent message = " + result + "with Offset = " + result.getRecordMetadata().offset());
            }
        });
    }
}
