package com.Fawrybook.Fawrybook.service;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.enums.ReactionType;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.model.PostReaction;
import com.Fawrybook.Fawrybook.repository.PostReactionRepository;
import com.Fawrybook.Fawrybook.repository.PostRepository;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class LikeService {
    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final JwtUtil jwtUtil;

    public LikeService(PostRepository postRepository , PostReactionRepository postReactionRepository,JwtUtil jwtUtil ) {
        this.postRepository = postRepository;
        this.postReactionRepository = postReactionRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<ApiResponse<Post>> likePost(Long postId, String token) {
        Long userId = jwtUtil.extractUserId(token);
        System.out.println("userId+ " + userId);

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), "Post not found", null));
        }

        Post post = postOptional.get();

        Optional<PostReaction> existingReaction = postReactionRepository.findByUserIdAndPost(userId, post);

        if (existingReaction.isPresent()) {
            PostReaction reaction = existingReaction.get();

            if (reaction.getReactionType() == ReactionType.LIKE) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), "You have already liked this post", post));
            }

            reaction.setReactionType(ReactionType.LIKE);
            postReactionRepository.save(reaction);

            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);

            return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), "Post liked successfully", post));
        }

        PostReaction reaction = new PostReaction();
        reaction.setUserId(userId);
        reaction.setPost(post);
        reaction.setReactionType(ReactionType.LIKE);
        postReactionRepository.save(reaction);

        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);

        return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), "Post liked successfully", post));
    }


    public ResponseEntity<ApiResponse<Post>> dislikePost(Long postId, String token) {
        Long userId = jwtUtil.extractUserId(token);
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), "Post not found", null));
        }

        Post post = postOptional.get();

        Optional<PostReaction> existingReaction = postReactionRepository.findByUserIdAndPost(userId, post);

        if (existingReaction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), "You must like the post before disliking", post));
        }

        PostReaction reaction = existingReaction.get();

        if (reaction.getReactionType() == ReactionType.DISLIKE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), "You have already disliked this post", post));
        }

        reaction.setReactionType(ReactionType.DISLIKE);
        postReactionRepository.save(reaction);

        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);

        return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), "Post disliked successfully", post));
    }

}
