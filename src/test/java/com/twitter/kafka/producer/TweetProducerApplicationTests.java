package com.twitter.kafka.producer;

import com.twitter.kafka.producer.service.Producer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import twitter4j.TwitterException;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@EmbeddedKafka
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TweetProducerApplicationTests {

	@Autowired
	Producer producer;

	@Test
	void testConnectionWithTwitter() throws TwitterException {
		producer.connectWithTwitter();
	}

	@Test
	void pushtoKafka() {
		producer.sendMessage("hello kafka");
	}

}
