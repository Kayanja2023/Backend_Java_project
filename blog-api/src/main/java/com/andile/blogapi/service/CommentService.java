package com.andile.blogapi.service;

import com.andile.blogapi.dto.CommentDto;
import com.andile.blogapi.dto.CreateCommentDto;
import com.andile.blogapi.entity.Comment;
import com.andile.blogapi.entity.Post;
import com.andile.blogapi.entity.User;
import com.andile.blogapi.repositories.CommentRepository;
import com.andile.blogapi.repositories.PostRepository;
import com.andile.blogapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<CommentDto> getCommentsByAuthor(Long authorId) {
        return commentRepository.findByAuthorId(authorId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return convertToDto(comment);
    }
    
    public CommentDto createComment(CreateCommentDto createCommentDto) {
        User author = userRepository.findById(createCommentDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + createCommentDto.getAuthorId()));
        
        Post post = postRepository.findById(createCommentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + createCommentDto.getPostId()));
        
        Comment comment = new Comment();
        comment.setContent(createCommentDto.getContent());
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }
    
    public CommentDto updateComment(Long id, String content) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        
        existingComment.setContent(content);
        Comment updatedComment = commentRepository.save(existingComment);
        return convertToDto(updatedComment);
    }
    
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
    
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