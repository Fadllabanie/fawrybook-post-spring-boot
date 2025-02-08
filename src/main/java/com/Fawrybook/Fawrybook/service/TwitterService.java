package com.Fawrybook.Fawrybook.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterService {

    private final Twitter twitter;

    public TwitterService(
            @Value("${twitter.api.key}") String apiKey,
            @Value("${twitter.api.secret}") String apiSecret,
            @Value("${twitter.access.token}") String accessToken,
            @Value("${twitter.access.token.secret}") String accessTokenSecret
    ) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(apiKey)
                .setOAuthConsumerSecret(apiSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        this.twitter = tf.getInstance();
    }

    public String tweetPost(String message) {
        try {
            Status status = twitter.updateStatus(message);
            return "Tweet posted successfully: " + status.getText();
        } catch (Exception e) {
            return "Failed to post tweet: " + e.getMessage();
        }
    }
}
