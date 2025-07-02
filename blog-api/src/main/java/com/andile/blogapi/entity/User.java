package com.andile.blogapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;


 // Entity representing a blog user/author

@Entity
@Data
@Table(name = "users")
public class User {
    // Primary key - auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User's display name - required
    @NotBlank
    private String username;

    // User's email address - required and must be valid format
    @Email
    @NotBlank
    private String email;
    
    // User's password - stored securely
    @NotBlank
    private String password;

    // Posts authored by this user - cascades all operations, lazy loaded
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    // Comments authored by this user - cascades all operations, lazy loaded
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}