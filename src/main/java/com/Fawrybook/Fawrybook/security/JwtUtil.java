package com.Fawrybook.Fawrybook.security;

import com.Fawrybook.Fawrybook.repository.RevokedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Base64;

@Component
public class JwtUtil {

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    private final SecretKey secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        System.out.println("🔗 x1 ");
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, Long userId) {
        System.out.println("🔗 x2 ");
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // Store userId in JWT
        return createToken(claims, username);
    }


    private String createToken(Map<String, Object> claims, String subject) {
        System.out.println("🔗 x3");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        System.out.println("🔗 x4 ");
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public String extractUsername(String token) {
        System.out.println("🔗 x5 ");
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        System.out.println("🔗 x6 ");
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, String username) {
        System.out.println("🔗 x7 ");
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token) && !isTokenRevoked(token) ;
    }

    public boolean isTokenExpired(String token) {
        System.out.println("🔗 x8 ");
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Boolean isTokenRevoked(String token) {
        System.out.println("🔗 x9 ");
        return revokedTokenRepository.findByToken(token).isPresent();
    }
}
