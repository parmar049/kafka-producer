package com.twitter.kafka.producer.controller;

import com.twitter.kafka.producer.service.TwitterFilteredStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity getFilteredTweetStream(@RequestParam String ruleFilter) throws IOException, URISyntaxException {
        log.info("<< Producing data stream >>");
        filteredStream.streamTweet(ruleFilter);
        return new ResponseEntity(HttpStatus.OK);
    }
}
