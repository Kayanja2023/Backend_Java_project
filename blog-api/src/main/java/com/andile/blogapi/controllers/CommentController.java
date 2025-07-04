package com.andile.blogapi.controllers;

import com.andile.blogapi.dto.CommentDto;

import com.andile.blogapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

// This class returns data in Json
@RestController
@RequestMapping("/api/v1/comments") //sets the base path for all endpoints
@RequiredArgsConstructor
public class CommentController {

    // Service layer responsible for comment-related business logic
    private final CommentService commentService;

    // Retrieves all comments associated with a specific blog post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    // Retrieves a single comment by its unique identifier
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    // Creates a new comment based on the provided payload
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // Updates the content of an existing comment identified by ID
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody String content) {
        CommentDto updatedComment = commentService.updateComment(id, content);
        return ResponseEntity.ok(updatedComment);
    }

    // Deletes a comment by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}