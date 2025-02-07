package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.dto.PostDTO;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import com.Fawrybook.Fawrybook.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;        // ✅ Correct Page import
import org.springframework.data.domain.Pageable;   // ✅ Correct Pageable import
import org.springframework.data.domain.PageRequest; // ✅ Correct PageRequest import


@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService,JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;

    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostDTO>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PostDTO> postDTOs = postService.getAllPosts(pageable);

        ApiResponse<Page<PostDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Posts retrieved successfully",
                postDTOs
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ApiResponse<Post>> createPost(@Valid @RequestBody Post post) {
    Post savedPost = postService.createPost(post);
        ApiResponse<Post> response = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Post created successfully",
                savedPost
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse<Post>> likePost(@PathVariable Long postId, HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);
        return postService.likePost(postId, userId);
    }

    @PostMapping("/{postId}/dislike")
    public ResponseEntity<ApiResponse<Post>> dislikePost(@PathVariable Long postId, HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.extractUserId(token);
        return postService.dislikePost(postId, userId);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
