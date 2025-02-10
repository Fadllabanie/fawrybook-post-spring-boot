package com.Fawrybook.Fawrybook.service;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TwitterService {

    private static final String TWEET_URL = "https://api.twitter.com/2/tweets";

    public String tweet(String message) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("AAAAAAAAAAAAAAAAAAAAAEZOvQEAAAAAySJtjJLbFa67quRCiN8veXgdd%2Bw%3DaFW0ym0bahjPne2CEiasE9HfYXDrzF4oIBgriPSdY53wUkXFQk"); // Use your Bearer Token
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = "{\"text\": \"" + message + "\"}";
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(TWEET_URL, HttpMethod.POST, request, String.class);

            return "Tweet posted successfully! Response: " + response.getBody();
        } catch (Exception e) {
            throw new Exception("Failed to post tweet: " + e.getMessage());
        }
    }
}