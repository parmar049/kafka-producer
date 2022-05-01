package com.twitter.kafka.producer.service;

import com.twitter.kafka.producer.config.TwitterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Producer {

    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(10000);

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void connectWithTwitter() throws TwitterException {
        ConfigurationBuilder confBuilder = new ConfigurationBuilder();
        confBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterConfig.CONSUMER_KEYS)
                .setOAuthConsumerSecret(TwitterConfig.CONSUMER_SECRETS)
                .setOAuthAccessToken(TwitterConfig.TOKEN)
                .setOAuthAccessTokenSecret(TwitterConfig.SECRET);
        TwitterFactory tf = new TwitterFactory(confBuilder.build());
        Twitter twitter = tf.getInstance();
        List<String> tweets = twitter.getHomeTimeline().stream()
                .map(item -> item.getText())
                .collect(Collectors.toList());
        System.out.println(tweets);

    }

    public void sendMessage(String message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(topic, UUID.randomUUID().toString(), message);
    }
}
