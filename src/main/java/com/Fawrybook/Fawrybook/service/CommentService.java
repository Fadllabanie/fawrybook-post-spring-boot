package com.Fawrybook.Fawrybook.service;

import com.Fawrybook.Fawrybook.dto.CommentDTO;
import com.Fawrybook.Fawrybook.exceptions.PostNotFoundException;
import com.Fawrybook.Fawrybook.exceptions.UnauthorizedActionException;
import com.Fawrybook.Fawrybook.helpers.TextHelper;
import com.Fawrybook.Fawrybook.model.Comment;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.repository.CommentRepository;
import com.Fawrybook.Fawrybook.repository.PostRepository;
import com.Fawrybook.Fawrybook.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository,JwtUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.jwtUtil = jwtUtil;
    }

    public CommentDTO addComment(@Valid Long postId, String text , HttpServletRequest request) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        String token = jwtUtil.extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        Post post = postOptional.get();

        text = TextHelper.extractPlainText(text);

        Comment comment = new Comment();
        comment.setText(text.trim());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUserId(userId);
        comment.setPost(post);

        comment = commentRepository.save(comment);

        return new CommentDTO(comment.getId(), comment.getText(), comment.getUserId(), comment.getCreatedAt());
    }

    public void deleteComment(Long commentId, HttpServletRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + commentId));

        String token = jwtUtil.extractToken(request);
        Long userId = jwtUtil.extractUserId(token);

        if (!comment.getUserId().equals(userId)) {
            throw new UnauthorizedActionException("User is not authorized to delete this comment");
        }

        commentRepository.deleteById(commentId);
    }

    public List<CommentDTO> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(comment -> new CommentDTO(comment.getId(), comment.getText(),comment.getUserId(), comment.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
