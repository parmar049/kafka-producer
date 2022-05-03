package com.twitter.kafka.producer.controller;

import com.twitter.kafka.producer.service.TwitterFilteredStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("v1/streamTweets")
@Slf4j
public class TwitterStreamController {

    private final TwitterFilteredStream filteredStream;

    @Autowired
    public TwitterStreamController(TwitterFilteredStream filteredStream) {
        this.filteredStream = filteredStream;
    }

    @GetMapping("/produceDataStream")
    public void getFilteredTweetStream() throws IOException, URISyntaxException {
        log.info("<< Producing data stream >>");
        filteredStream.streamTweet();
    }
}
