package com.andile.blogapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



 // Data Transfer Object for User entity

@Data
public class UserDto {
    // Unique identifier for the user
    private Long id;
    
    // User's display name - required
    @NotBlank(message = "Username cannot be empty")
    private String username;
    
    // User's email address - required and must be valid format
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
}