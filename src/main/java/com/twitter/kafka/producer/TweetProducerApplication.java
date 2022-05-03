package com.twitter.kafka.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TweetProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetProducerApplication.class, args);
	}

}
