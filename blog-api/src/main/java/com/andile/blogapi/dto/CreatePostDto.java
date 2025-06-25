package com.andile.blogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePostDto {
    @NotBlank
    private String title;
    
    @NotBlank
    private String content;
    
    @NotNull
    private Long authorId;
}