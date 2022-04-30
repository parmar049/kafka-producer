package com.twitter.kafka.producer.controller;

import com.twitter.kafka.producer.service.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/generateTweet")
@Slf4j
public class KafkaController {

    private final Producer kafkaProducer;

    public KafkaController(Producer producer) {
        this.kafkaProducer = producer;
    }

    @GetMapping("/push")
    public void pushTweet() {
        log.info("Pushing message : ");
        kafkaProducer.sendMessage("hello kafka");
    }
}
