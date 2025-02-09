package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/twitter")
public class TwitterController {

    @Autowired
    private TwitterService twitterService;

    @PostMapping("/tweet")
    public String sendTweet(@RequestParam String message) {
        return twitterService.postTweet(message);
    }
}