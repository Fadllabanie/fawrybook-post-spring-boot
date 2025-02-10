package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;

@RestController
@RequestMapping("/api/twitter")
public class TwitterController {

    @Autowired
    private TwitterService twitterService;

    @PostMapping(value = "/tweet", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String tweet(@RequestBody String message) {
        try {
            return twitterService.tweet(message);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("test")
    public static void test() throws TwitterException {
        TwitterFactory tf = new TwitterFactory();
        Twitter twitter = tf.getInstance();

        twitter.updateStatus("Hello World!");
    }
}