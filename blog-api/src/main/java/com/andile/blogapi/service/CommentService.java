package com.andile.blogapi.service;

import com.andile.blogapi.dto.CommentDto;
import com.andile.blogapi.entity.Comment;
import com.andile.blogapi.entity.Post;
import com.andile.blogapi.entity.User;
import com.andile.blogapi.exception.ApiException;
import com.andile.blogapi.repositories.CommentRepository;
import com.andile.blogapi.repositories.PostRepository;
import com.andile.blogapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


 // Service layer for comment operations

@Service
@RequiredArgsConstructor
public class CommentService {

    // Repository dependencies
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Get all comments for a specific post, ordered by creation time
    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get a single comment by ID
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Comment not found"));
        return convertToDto(comment);
    }

    // Create a new comment
    public CommentDto createComment(CommentDto commentDto) {
        User author = userRepository.findById(commentDto.getAuthorId())
                .orElseThrow(() -> new ApiException("Author not found"));

        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new ApiException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    // Update comment content
    public CommentDto updateComment(Long id, String content) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Comment not found"));

        existingComment.setContent(content);
        Comment updatedComment = commentRepository.save(existingComment);
        return convertToDto(updatedComment);
    }

    // Delete a comment by ID
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ApiException("Comment not found");
        }
        commentRepository.deleteById(id);
    }
    
    // Convert Comment entity to CommentDto
    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorUsername(comment.getAuthor().getUsername());
        dto.setPostId(comment.getPost().getId());
        return dto;
    }
}