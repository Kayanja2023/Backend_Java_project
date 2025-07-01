package com.andile.blogapi.service;

import com.andile.blogapi.dto.CommentDto;
import com.andile.blogapi.entity.Comment;
import com.andile.blogapi.entity.Post;
import com.andile.blogapi.entity.User;
import com.andile.blogapi.exception.ApiException;
import com.andile.blogapi.repositories.CommentRepository;
import com.andile.blogapi.repositories.PostRepository;
import com.andile.blogapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CommentService
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment testComment;
    private User testUser;
    private Post testPost;
    private CommentDto testCommentDto;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Create test post
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");

        // Create test comment
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Test comment");
        testComment.setAuthor(testUser);
        testComment.setPost(testPost);
        testComment.setCreatedAt(LocalDateTime.now());

        // Create test DTO
        testCommentDto = new CommentDto();
        testCommentDto.setContent("Test comment");
        testCommentDto.setAuthorId(1L);
        testCommentDto.setPostId(1L);
    }

    @Test
    void getCommentById_ShouldReturnComment_WhenCommentExists() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // When
        CommentDto result = commentService.getCommentById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test comment", result.getContent());
        assertEquals(1L, result.getAuthorId());
        assertEquals(1L, result.getPostId());
        verify(commentRepository).findById(1L);
    }

    @Test
    void getCommentById_ShouldThrowException_WhenCommentNotFound() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, 
            () -> commentService.getCommentById(1L));
        
        assertEquals("Comment not found", exception.getMessage());
    }

    @Test
    void createComment_ShouldCreateComment_WhenValidData() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // When
        CommentDto result = commentService.createComment(testCommentDto);

        // Then
        assertNotNull(result);
        assertEquals("Test comment", result.getContent());
        verify(userRepository).findById(1L);
        verify(postRepository).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_ShouldThrowException_WhenAuthorNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, 
            () -> commentService.createComment(testCommentDto));
        
        assertEquals("Author not found", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createComment_ShouldThrowException_WhenPostNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, 
            () -> commentService.createComment(testCommentDto));
        
        assertEquals("Post not found", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
    }
    
    @Test
    void getCommentsByPost_ShouldReturnComments() {
        // Given
        List<Comment> comments = Arrays.asList(testComment);
        when(commentRepository.findByPostIdOrderByCreatedAtAsc(1L)).thenReturn(comments);
        
        // When
        List<CommentDto> result = commentService.getCommentsByPost(1L);
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getContent());
        verify(commentRepository).findByPostIdOrderByCreatedAtAsc(1L);
    }
    
    @Test
    void updateComment_ShouldUpdateComment_WhenExists() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        
        // When
        CommentDto result = commentService.updateComment(1L, "Updated content");
        
        // Then
        assertNotNull(result);
        verify(commentRepository).findById(1L);
        verify(commentRepository).save(any(Comment.class));
    }
    
    @Test
    void updateComment_ShouldThrowException_WhenNotFound() {
        // Given
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        ApiException exception = assertThrows(ApiException.class,
            () -> commentService.updateComment(1L, "Updated content"));
        
        assertEquals("Comment not found", exception.getMessage());
    }
    
    @Test
    void deleteComment_ShouldDeleteComment_WhenExists() {
        // Given
        when(commentRepository.existsById(1L)).thenReturn(true);
        
        // When
        commentService.deleteComment(1L);
        
        // Then
        verify(commentRepository).existsById(1L);
        verify(commentRepository).deleteById(1L);
    }
    
    @Test
    void deleteComment_ShouldThrowException_WhenNotExists() {
        // Given
        when(commentRepository.existsById(1L)).thenReturn(false);
        
        // When & Then
        ApiException exception = assertThrows(ApiException.class,
            () -> commentService.deleteComment(1L));
        
        assertEquals("Comment not found", exception.getMessage());
    }
}