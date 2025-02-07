package com.Fawrybook.Fawrybook.repository;

import com.Fawrybook.Fawrybook.enums.ReactionType;
import com.Fawrybook.Fawrybook.model.PostReaction;
import com.Fawrybook.Fawrybook.model.User;
import com.Fawrybook.Fawrybook.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPostAndReactionType(User user, Post post, ReactionType reactionType);
}
