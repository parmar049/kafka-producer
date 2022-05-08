package com.twitter.kafka.producer;

import com.twitter.kafka.producer.config.TwitterConfig;
import com.twitter.kafka.producer.service.TwitterFilteredStreamImpl;
import com.twitter.kafka.producer.service.KafkaDataProducerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@EmbeddedKafka
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TweetProducerApplicationTests {

	@Autowired
	KafkaDataProducerImpl producer;

	@Autowired
	TwitterConfig twitterConfig;

	//@Test
	void testTweeterStreamAPI() throws IOException, URISyntaxException {
		new TwitterFilteredStreamImpl(producer, twitterConfig).streamTweet("eid");
	}

	@Test
	//Test will fail if its unable to push the message to Kafka topic
	void pushtoKafka() {
		producer.sendMessage("hello kafka");
	}

}
