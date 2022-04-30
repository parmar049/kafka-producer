package com.twitter.kafka.producer;

import com.twitter.kafka.producer.service.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TweetProducerApplicationTests {

	@Test
	void pushtoKafka() {
		Producer producer = new Producer();
		producer.sendMessage("hello kafka");
	}

}
