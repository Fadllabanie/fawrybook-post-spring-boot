package com.Fawrybook.Fawrybook.service;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.dto.CommentDTO;
import com.Fawrybook.Fawrybook.dto.PostDTO;
import com.Fawrybook.Fawrybook.enums.ReactionType;
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

        return posts.map(post -> {
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(comment -> new CommentDTO(comment.getId(), comment.getText(), comment.getCreatedAt()))
                    .collect(Collectors.toList());

            return new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(), post.getLikes(), commentDTOs);
        });
    }


    public Post getPost(Long id) {
        return postRepository.getById(id);
    }

    public Post createPost(@Valid Post post) {
        return postRepository.save(post);
    }

    public ResponseEntity<ApiResponse<Post>> likePost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isEmpty() || userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), "Post or User not found", null));
        }

        Post post = postOptional.get();
        User user = userOptional.get();

        if (postReactionRepository.existsByUserAndPostAndReactionType(user, post, ReactionType.LIKE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), "You have already liked this post", post));
        }

        PostReaction reaction = new PostReaction();
        reaction.setUser(user);
        reaction.setPost(post);
        reaction.setReactionType(ReactionType.LIKE);
        postReactionRepository.save(reaction);

        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);

        return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), "Post liked successfully", post));
    }

    public ResponseEntity<ApiResponse<Post>> dislikePost(Long postId, Long userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isEmpty() || userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), "Post or User not found", null));
        }

        Post post = postOptional.get();
        User user = userOptional.get();

        if (postReactionRepository.existsByUserAndPostAndReactionType(user, post, ReactionType.DISLIKE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), "You have already disliked this post", post));
        }

        PostReaction reaction = new PostReaction();
        reaction.setUser(user);
        reaction.setPost(post);
        reaction.setReactionType(ReactionType.DISLIKE);
        postReactionRepository.save(reaction);

        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);

        return ResponseEntity.ok(new ApiResponse<>(true, HttpStatus.OK.value(), "Post disliked successfully", post));
    }
}
