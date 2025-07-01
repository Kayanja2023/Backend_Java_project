package com.andile.blogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


 //Data Transfer Object for Post entity

@Data
public class PostDto {
    // Unique identifier for the post
    private Long id;
    
    // Post title - required field
    @NotBlank(message = "Title cannot be empty")
    private String title;
    
    // Post content/body - required field
    @NotBlank(message = "Content cannot be empty")
    private String content;
    
    // Timestamp when post was created
    private LocalDateTime createdAt;
    
    // ID of the user who wrote the post - required
    @NotNull(message = "Author ID is required")
    private Long authorId;
    
    // Username of the post author for display
    private String authorUsername;
}