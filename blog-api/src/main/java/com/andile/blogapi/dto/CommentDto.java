package com.andile.blogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


  //Data Transfer Object for Comment entity

@Data
public class CommentDto {
    // Unique identifier - null for creation, populated for retrieval
    private Long id;
    
    // Comment content - required
    @NotBlank(message = "Comment content cannot be empty")
    private String content;
    
    // Timestamp - null for creation, populated by system
    private LocalDateTime createdAt;
    
    // Author ID - required
    @NotNull(message = "Author ID is required")
    private Long authorId;
    
    // Author username - null for creation, populated for retrieval
    private String authorUsername;
    
    // Post ID - required
    @NotNull(message = "Post ID is required")
    private Long postId;
}