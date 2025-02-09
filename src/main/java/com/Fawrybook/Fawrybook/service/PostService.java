package com.Fawrybook.Fawrybook.service;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.dto.CommentDTO;
import com.Fawrybook.Fawrybook.dto.PostDTO;
import com.Fawrybook.Fawrybook.enums.ReactionType;
import com.Fawrybook.Fawrybook.exceptions.PostNotFoundException;
import com.Fawrybook.Fawrybook.exceptions.UserAlreadyExistsException;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.model.PostReaction;
import com.Fawrybook.Fawrybook.model.User;
import com.Fawrybook.Fawrybook.repository.PostReactionRepository;
import com.Fawrybook.Fawrybook.repository.PostRepository;
import com.Fawrybook.Fawrybook.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class PostService {
    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository ,PostReactionRepository postReactionRepository,UserRepository userRepository ) {
        this.postRepository = postRepository;
        this.postReactionRepository = postReactionRepository;
        this.userRepository = userRepository;
    }
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        double averageLikes = calculateAverageLikes();

        return posts.map(post -> {
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(comment -> new CommentDTO(comment.getId(), comment.getText(), comment.getCreatedAt()))
                    .collect(Collectors.toList());

            return new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(), post.getLikes(), commentDTOs, averageLikes,post.getTwitterShareUrl());
        });
    }


    private double calculateAverageLikes() {
        long totalLikes = postRepository.findAll().stream().mapToLong(Post::getLikes).sum();
        long totalPosts = postRepository.count();

        if (totalPosts == 0) return 0.0; // Prevent division by zero

        return (double) totalLikes / totalPosts;
    }

    public Post getPost(Long id) {
        return postRepository.getById(id);
    }

    public Post createPost(@Valid Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long postId, Post updatedPost) {
        return postRepository.findById(postId).map(existingPost -> {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            return postRepository.save(existingPost);
        }).orElseThrow(() -> new RuntimeException("Post not found with id " + postId));
    }


    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found with id " + postId);
        }
        postRepository.deleteById(postId);
    }




    public String getMessageFromPost(Long postId) {
        Post post =  postRepository.getById(postId);
        return  post.getContent();
    }
}
