package com.Fawrybook.Fawrybook.security;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.service.AuthServiceFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthServiceFeignClient authServiceFeignClient;

    public JwtFilter(AuthServiceFeignClient authServiceFeignClient) {
        this.authServiceFeignClient = authServiceFeignClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            System.out.println("Extracted Token: " + token);
            System.out.println("Sending token to auth-service for validation...");

            boolean isValid = validateTokenWithAuthService(token);
            if (!isValid) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                ApiResponse<Object> errorResponse = new ApiResponse<>(
                        false,
                        HttpStatus.UNAUTHORIZED.value(),
                        "Unauthorized - Invalid token",
                        null
                );

                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                response.getWriter().flush();
                return;
            }

            System.out.println("Token Validation Successful!");

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(null, null, null);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }

    private boolean validateTokenWithAuthService(String token) {
        try {
            System.out.println("Sending token to auth-service for validation...");

            Map<String, Object> response = authServiceFeignClient.checkToken("Bearer " + token);

            System.out.println("Token Validation Response: " + response);
            return response != null && Boolean.TRUE.equals(response.get("valid"));
        } catch (Exception e) {
            System.out.println("Error in Token Validation: " + e.getMessage());
            return false;
        }
    }

}
