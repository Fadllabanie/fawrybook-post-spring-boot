package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.exceptions.UserAlreadyExistsException;
import com.Fawrybook.Fawrybook.model.RevokedToken;
import com.Fawrybook.Fawrybook.repository.RevokedTokenRepository;
import com.Fawrybook.Fawrybook.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RevokedTokenRepository revokedTokenRepository;
    public AuthController(AuthService authService,RevokedTokenRepository revokedTokenRepository) {
        this.authService = authService;
        this.revokedTokenRepository = revokedTokenRepository;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");

            String token = authService.registerUser(request.get("username"), request.get("password"));
            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "token", token
            ));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String token = authService.authenticateUser(request.get("username"), request.get("password"));
        return Map.of("token", token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {

        String jwtToken = token.substring(7);
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(jwtToken);
        revokedToken.setRevokedAt(LocalDateTime.now());
        revokedTokenRepository.save(revokedToken);

        return ResponseEntity.ok(Map.of(
                "status", 1,
                "http_code", 200,
                "message", "Successfully logged out"
        ));
    }
}
