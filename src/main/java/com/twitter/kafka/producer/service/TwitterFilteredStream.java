package com.twitter.kafka.producer.service;

public interface TwitterFilteredStream {

    void streamTweet(String filterValue);
}
