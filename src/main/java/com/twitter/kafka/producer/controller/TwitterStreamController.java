package com.twitter.kafka.producer.controller;

import com.twitter.kafka.producer.service.TwitterFilteredStreamImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("v1/streamTweets")
@Slf4j
public class TwitterStreamController {

    private final TwitterFilteredStreamImpl filteredStream;

    @Autowired
    public TwitterStreamController(TwitterFilteredStreamImpl filteredStream) {
        this.filteredStream = filteredStream;
    }

    @GetMapping("/produceDataStream")
    public void getFilteredTweetStream(@RequestParam String ruleFilter) throws IOException, URISyntaxException {
        log.info("<< Producing data stream >>");
        filteredStream.streamTweet(ruleFilter);
    }
}
