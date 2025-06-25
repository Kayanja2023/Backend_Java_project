package com.andile.blogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCommentDto {
    @NotBlank
    private String content;
    
    @NotNull
    private Long authorId;
    
    @NotNull
    private Long postId;
}