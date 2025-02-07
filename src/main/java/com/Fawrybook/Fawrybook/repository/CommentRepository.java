package com.Fawrybook.Fawrybook.repository;

import com.Fawrybook.Fawrybook.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
        List<Comment> findByPostId(Long postId); // ✅ Find comments by post ID
}
