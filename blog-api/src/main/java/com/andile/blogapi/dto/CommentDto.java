package com.andile.blogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    
    @NotBlank
    private String content;
    
    private LocalDateTime createdAt;
    
    @NotNull
    private Long authorId;
    
    private String authorUsername;
    
    @NotNull
    private Long postId;
}