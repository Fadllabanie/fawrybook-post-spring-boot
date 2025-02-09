package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.dto.PostDTO;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import com.Fawrybook.Fawrybook.service.LikeService;
import com.Fawrybook.Fawrybook.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/posts")
public class LikeController {
    private final LikeService likeService;
    private final JwtUtil jwtUtil;

    public LikeController(LikeService likeService, JwtUtil jwtUtil) {
        this.likeService = likeService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Post>> likePost(@PathVariable Long postId, HttpServletRequest request) {
        String token = extractToken(request);
        return likeService.likePost(postId, token);
    }

    @PostMapping("/{postId}/dislike")
    public ResponseEntity<ApiResponse<Post>> dislikePost(@PathVariable Long postId, HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);
        return likeService.dislikePost(postId, token);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
