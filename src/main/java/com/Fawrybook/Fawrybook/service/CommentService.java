package com.Fawrybook.Fawrybook.service;

import com.Fawrybook.Fawrybook.dto.CommentDTO;
import com.Fawrybook.Fawrybook.helpers.TextHelper;
import com.Fawrybook.Fawrybook.model.Comment;
import com.Fawrybook.Fawrybook.model.Post;
import com.Fawrybook.Fawrybook.repository.CommentRepository;
import com.Fawrybook.Fawrybook.repository.PostRepository;
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

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CommentDTO addComment(@Valid Long postId, String text) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Post post = postOptional.get();

        text = TextHelper.extractPlainText(text);

        Comment comment = new Comment();
        comment.setText(text.trim());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);

        comment = commentRepository.save(comment);

        return new CommentDTO(comment.getId(), comment.getText(), comment.getCreatedAt());
    }

    public List<CommentDTO> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(comment -> new CommentDTO(comment.getId(), comment.getText(), comment.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
