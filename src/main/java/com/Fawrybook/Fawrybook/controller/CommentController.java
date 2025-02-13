package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.dto.CommentDTO;
import com.Fawrybook.Fawrybook.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> list(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsForPost(postId);
        ApiResponse<List<CommentDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Comments retrieved successfully",
                comments
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommentDTO>> create(@Valid @PathVariable Long postId, @RequestBody String text, HttpServletRequest request) {
        CommentDTO commentDTO = commentService.addComment(postId, text,request);
        ApiResponse<CommentDTO> response = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Comment added successfully",
                commentDTO
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long commentId,HttpServletRequest request) {
        commentService.deleteComment(commentId,request);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "comment deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }

}
