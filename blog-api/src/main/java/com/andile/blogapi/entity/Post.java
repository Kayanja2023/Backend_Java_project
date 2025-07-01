package com.andile.blogapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


  //Entity representing a blog post

@Entity
@Data
public class Post {
    // Primary key - auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Post title - required
    @NotBlank
    private String title;

    // Post content/body - stored as TEXT for longer content
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    // Timestamp when post was created - defaults to now
    private LocalDateTime createdAt = LocalDateTime.now();

    // User who wrote the post - lazy loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Comments on this post - cascades all operations, lazy loaded
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}