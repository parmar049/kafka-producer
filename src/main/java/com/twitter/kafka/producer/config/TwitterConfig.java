package com.twitter.kafka.producer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("twitter-config")
@Configuration
@Getter
@Setter
public class TwitterConfig {

    private String stream_api_url;

    private String bearer_access_token;

}
