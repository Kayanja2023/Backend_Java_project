package com.andile.blogapi.repositories;

import com.andile.blogapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


  //Repository interface for Comment entity operations

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Find all comments for a specific post
    List<Comment> findByPostId(Long postId);
    
    // Find all comments for a post ordered by creation time
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}