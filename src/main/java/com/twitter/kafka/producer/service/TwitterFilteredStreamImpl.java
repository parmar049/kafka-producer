package com.twitter.kafka.producer.service;

import com.twitter.kafka.producer.config.TwitterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TwitterFilteredStreamImpl implements TwitterFilteredStream {

    private final KafkaDataProducerImpl producer;

    private final TwitterConfig twitterConfig;

    @Autowired
    public TwitterFilteredStreamImpl(KafkaDataProducerImpl producer, TwitterConfig twitterConfig) throws IOException, URISyntaxException {
        this.producer = producer;
        this.twitterConfig = twitterConfig;
    }

    @Override
    public void streamTweet(String filterValue) {
        Map<String, String> rules = new HashMap<>();
        rules.put(filterValue, filterValue.concat("-tweets"));
        setupRules(rules);
        connectStream();
    }

    /*
     * This method calls the filtered stream endpoint and streams Tweets from it
     */
    private void connectStream() {

        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build()).build();

        URIBuilder uriBuilder = null;
        HttpGet httpGet = null;
        try {
            uriBuilder = new URIBuilder(twitterConfig.getStream_api_url());
            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            log.error("Syntex error in HTTP URL, {}", uriBuilder.getPath());
            e.printStackTrace();
        }
        httpGet.setHeader("Authorization", String.format("Bearer %s", twitterConfig.getBearer_access_token()));
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader((entity.getContent())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String line = null;
                line = reader.readLine();
                while (line != null) {
                    log.info(line);
                    log.info("Pushing data to kafka ::");
                    producer.sendMessage(line);
                    line = reader.readLine();
                }

            }

        } catch (IOException e) {
            log.error("IOException while trying to execute httpClient");
            e.printStackTrace();
        }


    }

    /*
     * Helper method to setup rules before streaming data
     * */
    private void setupRules(Map<String, String> rules) {
        List<String> existingRules = getRules();
        if (existingRules.size() > 0) {
            deleteRules(existingRules);
        }
        createRules(rules);
    }

    /*
     * Helper method to create rules for filtering
     * */
    private void createRules(Map<String, String> rules) {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = null;
        HttpPost httpPost = null;
        try {
            uriBuilder = new URIBuilder(twitterConfig.getStream_api_url().concat("/rules"));
            httpPost = new HttpPost(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        httpPost.setHeader("Authorization", String.format("Bearer %s", twitterConfig.getBearer_access_token()));
        httpPost.setHeader("content-type", "application/json");
        StringEntity body = null;
        try {
            body = new StringEntity(getFormattedString("{\"add\": [%s]}", rules));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(body);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                log.info(EntityUtils.toString(entity, "UTF-8"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Helper method to get existing rules
     * */
    private List<String> getRules() {
        List<String> rules = new ArrayList<>();
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = null;
        HttpGet httpGet = null;
        try {
            uriBuilder = new URIBuilder(twitterConfig.getStream_api_url().concat("/rules"));
            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        httpGet.setHeader("Authorization", String.format("Bearer %s", twitterConfig.getBearer_access_token()));
        httpGet.setHeader("content-type", "application/json");
        HttpResponse response = null;
        HttpEntity entity = null;
        JSONObject json = null;
        try {
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            if (null != entity) {
                json = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
                if (json.length() > 1) {
                    JSONArray array = (JSONArray) json.get("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        rules.add(jsonObject.getString("id"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rules;
    }

    /*
     * Helper method to delete rules
     * */
    private void deleteRules(List<String> existingRules) {
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = null;
        HttpPost httpPost = null;
        try {
            uriBuilder = new URIBuilder(twitterConfig.getStream_api_url().concat("/rules"));
            httpPost = new HttpPost(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        httpPost.setHeader("Authorization", String.format("Bearer %s", twitterConfig.getBearer_access_token()));
        httpPost.setHeader("content-type", "application/json");
        StringEntity body = null;
        try {
            body = new StringEntity(getFormattedString("{ \"delete\": { \"ids\": [%s]}}", existingRules));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(body);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                log.info(EntityUtils.toString(entity, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getFormattedString(String string, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        if (ids.size() == 1) {
            return String.format(string, "\"" + ids.get(0) + "\"");
        } else {
            for (String id : ids) {
                sb.append("\"" + id + "\"" + ",");
            }
            String result = sb.toString();
            return String.format(string, result.substring(0, result.length() - 1));
        }
    }

    private static String getFormattedString(String string, Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        if (rules.size() == 1) {
            String key = rules.keySet().iterator().next();
            return String.format(string, "{\"value\": \"" + key + "\", \"tag\": \"" + rules.get(key) + "\"}");
        } else {
            for (Map.Entry<String, String> entry : rules.entrySet()) {
                String value = entry.getKey();
                String tag = entry.getValue();
                sb.append("{\"value\": \"" + value + "\", \"tag\": \"" + tag + "\"}" + ",");
            }
            String result = sb.toString();
            return String.format(string, result.substring(0, result.length() - 1));
        }
    }

}