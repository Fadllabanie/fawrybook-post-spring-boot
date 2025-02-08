package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.service.PostService;
import com.Fawrybook.Fawrybook.service.TwitterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/twitter")
public class TwitterController {

    private final TwitterService twitterService;
    private final PostService postService;


    public TwitterController(TwitterService twitterService,PostService postService) {
        this.twitterService = twitterService;
        this.postService = postService;
    }

    @PostMapping("/tweet")
    public ResponseEntity<String> tweetPost(@RequestBody Map<String, Long> request) {

        Long postId = request.get("postId");
        String message = postService.getMessageFromPost(postId);
        String response = twitterService.tweetPost(message);
        return ResponseEntity.ok(response);
    }
}
