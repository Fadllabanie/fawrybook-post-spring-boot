package com.Fawrybook.Fawrybook.repository;

import com.Fawrybook.Fawrybook.enums.ReactionType;
import com.Fawrybook.Fawrybook.model.PostReaction;
import com.Fawrybook.Fawrybook.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByUserIdAndPost(Long userId, Post post);
    boolean existsByUserIdAndPostAndReactionType(Long userId, Post post, ReactionType reactionType);
}

