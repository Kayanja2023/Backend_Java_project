package com.andile.blogapi.repositories;

import com.andile.blogapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


 // Repository interface for Post entity operations

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Find all posts by a specific author
    List<Post> findByAuthorId(Long authorId);
    
    // Search posts by title (case-insensitive partial match)
    List<Post> findByTitleContainingIgnoreCase(String title);
    
    // Get all posts ordered by newest first
    List<Post> findAllByOrderByCreatedAtDesc();
}