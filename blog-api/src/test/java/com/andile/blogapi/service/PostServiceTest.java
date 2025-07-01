package com.andile.blogapi.service;

import com.andile.blogapi.dto.PostDto;
import com.andile.blogapi.entity.Post;
import com.andile.blogapi.entity.User;
import com.andile.blogapi.exception.ApiException;
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
 * Unit tests for PostService
 */
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private Post testPost;
    private User testUser;
    private PostDto testPostDto;

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
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);
        testPost.setCreatedAt(LocalDateTime.now());

        // Create test DTO
        testPostDto = new PostDto();
        testPostDto.setTitle("Test Post");
        testPostDto.setContent("Test content");
        testPostDto.setAuthorId(1L);
    }

    @Test
    void getPostById_ShouldReturnPost_WhenPostExists() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        PostDto result = postService.getPostById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
        assertEquals("Test content", result.getContent());
        verify(postRepository).findById(1L);
    }

    @Test
    void getPostById_ShouldThrowException_WhenPostNotFound() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, 
            () -> postService.getPostById(1L));
        
        assertEquals("Post not found", exception.getMessage());
    }

    @Test
    void createPost_ShouldCreatePost_WhenValidData() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        PostDto result = postService.createPost(testPostDto);

        // Then
        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
        verify(userRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void createPost_ShouldThrowException_WhenAuthorNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, 
            () -> postService.createPost(testPostDto));
        
        assertEquals("Author not found", exception.getMessage());
        verify(postRepository, never()).save(any(Post.class));
    }
    
    @Test
    void getAllPosts_ShouldReturnAllPosts() {
        // Given
        List<Post> posts = Arrays.asList(testPost);
        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(posts);
        
        // When
        List<PostDto> result = postService.getAllPosts();
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Test Post", result.get(0).getTitle());
        verify(postRepository).findAllByOrderByCreatedAtDesc();
    }
    
    @Test
    void updatePost_ShouldUpdatePost_WhenValidData() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        
        PostDto updateDto = new PostDto();
        updateDto.setTitle("Updated Title");
        updateDto.setContent("Updated Content");
        
        // When
        PostDto result = postService.updatePost(1L, updateDto);
        
        // Then
        assertNotNull(result);
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }
    
    @Test
    void updatePost_ShouldThrowException_WhenPostNotFound() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        
        // When & Then
        ApiException exception = assertThrows(ApiException.class,
            () -> postService.updatePost(1L, testPostDto));
        
        assertEquals("Post not found", exception.getMessage());
    }
    
    @Test
    void deletePost_ShouldDeletePost_WhenExists() {
        // Given
        when(postRepository.existsById(1L)).thenReturn(true);
        
        // When
        postService.deletePost(1L);
        
        // Then
        verify(postRepository).existsById(1L);
        verify(postRepository).deleteById(1L);
    }
    
    @Test
    void deletePost_ShouldThrowException_WhenNotExists() {
        // Given
        when(postRepository.existsById(1L)).thenReturn(false);
        
        // When & Then
        ApiException exception = assertThrows(ApiException.class,
            () -> postService.deletePost(1L));
        
        assertEquals("Post not found", exception.getMessage());
    }
}