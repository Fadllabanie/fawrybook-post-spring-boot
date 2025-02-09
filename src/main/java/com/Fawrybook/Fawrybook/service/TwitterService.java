package com.Fawrybook.Fawrybook.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Service
public class TwitterService {

    @Value("${twitter.api.key}")
    private String apiKey;

    @Value("${twitter.api.secret}")
    private String apiSecret;

    @Value("${twitter.access.token}")
    private String accessToken;

    @Value("${twitter.access.secret}")
    private String accessSecret;

    private Twitter getTwitterInstance() {
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(apiKey, apiSecret);
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessSecret));
        return twitter;
    }

    public String postTweet(String tweetText) {
        try {
            Twitter twitter = getTwitterInstance();
            Status status = twitter.updateStatus(tweetText);
            return "Tweet posted successfully: " + status.getText();
        } catch (TwitterException e) {
            return "Failed to post tweet: " + e.getErrorMessage();
        }
    }
}