package com.Fawrybook.Fawrybook.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "fawrybook-auth", url = "http://api-gateway:8765/fawrybook-auth/api/v1/auth")
public interface AuthServiceFeignClient {

    @PostMapping(value = "/check-token", consumes = "application/json")
    Map<String, Object> checkToken(@RequestHeader("Authorization") String token);

    @PostMapping(value = "/extract-user-id", consumes = "application/json")
    Map<String, Object> extractUserId(@RequestHeader("Authorization") String token);
}
