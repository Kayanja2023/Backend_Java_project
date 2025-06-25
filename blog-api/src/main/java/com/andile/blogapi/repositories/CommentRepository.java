package com.andile.blogapi.repositories;

import com.andile.blogapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByAuthorId(Long authorId);
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}