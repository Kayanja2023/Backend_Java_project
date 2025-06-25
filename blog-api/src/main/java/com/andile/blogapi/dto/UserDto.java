package com.andile.blogapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    
    @NotBlank
    private String username;
    
    @Email
    @NotBlank
    private String email;
}