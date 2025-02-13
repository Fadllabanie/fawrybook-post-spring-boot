package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import com.Fawrybook.Fawrybook.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
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
        String token = jwtUtil.extractToken(request);
        return likeService.likePost(postId, token);
    }

    @PostMapping("/{postId}/dislike")
    public ResponseEntity<ApiResponse<Post>> dislikePost(@PathVariable Long postId, HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);

        return likeService.dislikePost(postId, token);
    }


}
