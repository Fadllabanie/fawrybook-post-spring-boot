package com.Fawrybook.Fawrybook.service;

import com.Fawrybook.Fawrybook.dto.CommentDTO;
import com.Fawrybook.Fawrybook.dto.PostDTO;
import com.Fawrybook.Fawrybook.exceptions.PostNotFoundException;
import com.Fawrybook.Fawrybook.exceptions.UnauthorizedActionException;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.repository.PostRepository;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public PostService(PostRepository postRepository ,JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.jwtUtil = jwtUtil;
    }
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        double averageLikes = calculateAverageLikes();

        return posts.map(post -> {
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(comment -> new CommentDTO(comment.getId(), comment.getText(),comment.getUserId(), comment.getCreatedAt()))
                    .collect(Collectors.toList());

            return new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.getUserId() ,post.getCreatedAt(), post.getLikes(), commentDTOs, averageLikes,post.getTwitterShareUrl());
        });
    }

    public Optional<PostDTO> getPostById(Long postId) {
        return postRepository.findById(postId).map(post -> {
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(comment -> new CommentDTO(comment.getId(), comment.getText(), comment.getUserId(), comment.getCreatedAt()))
                    .collect(Collectors.toList());

            double averageLikes = calculateAverageLikes(); 

            return new PostDTO(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getUserId(),
                    post.getCreatedAt(),
                    post.getLikes(),
                    commentDTOs,
                    averageLikes,
                    post.getTwitterShareUrl());
        });
    }

    private double calculateAverageLikes() {
        long totalLikes = postRepository.findAll().stream().mapToLong(Post::getLikes).sum();
        long totalPosts = postRepository.count();

        if (totalPosts == 0) return 0.0; 

        return (double) totalLikes / totalPosts;
    }

    public Post createPost(@Valid Post post, HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        Long userId = jwtUtil.extractUserId(token);
        post.setUserId(userId);
        return postRepository.save(post);
    }

    public Post updatePost(Long postId, Post updatedPost, HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id " + postId));


        if (userId != existingPost.getUserId()) {
            throw new UnauthorizedActionException("User is not authorized to update this post");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        return postRepository.save(existingPost);
    }

    public void deletePost(Long postId,HttpServletRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        String token = jwtUtil.extractToken(request);

        Long userId = jwtUtil.extractUserId(token);

        if (userId != post.getUserId()) {
            throw new UnauthorizedActionException("User is not authorized to delete this post");
        }
        postRepository.deleteById(postId);
    }

}
