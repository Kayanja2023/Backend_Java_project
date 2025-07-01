package com.andile.blogapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;


 // Entity representing a comment on a blog post

@Entity
@Data
public class Comment {
    // Primary key - auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Comment text content - stored as TEXT for longer content
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    // Timestamp when comment was created - defaults to now
    private LocalDateTime createdAt = LocalDateTime.now();

    // User who wrote the comment - lazy loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Post this comment belongs to - lazy loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}