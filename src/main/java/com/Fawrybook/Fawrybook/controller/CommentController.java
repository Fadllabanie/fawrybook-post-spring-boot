package com.Fawrybook.Fawrybook.controller;

import com.Fawrybook.Fawrybook.dto.ApiResponse;
import com.Fawrybook.Fawrybook.dto.CommentDTO;
import com.Fawrybook.Fawrybook.service.CommentService;
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

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommentDTO>> addComment(@Valid @PathVariable Long postId, @RequestBody String text) {
        CommentDTO commentDTO = commentService.addComment(postId, text); // ✅ Now using `CommentDTO`
        ApiResponse<CommentDTO> response = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Comment added successfully",
                commentDTO
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getComments(@PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsForPost(postId); // ✅ Now using `CommentDTO`
        ApiResponse<List<CommentDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Comments retrieved successfully",
                comments
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "comment deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }

}
