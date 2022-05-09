package com.twitter.kafka.producer.controller;

import com.twitter.kafka.producer.service.TwitterFilteredStreamImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public void getFilteredTweetStream(@RequestParam String ruleFilter) {
        log.info("<< Producing data stream >>");
        filteredStream.streamTweet(ruleFilter);
    }
}
