package com.andile.blogapi.controllers;

import com.andile.blogapi.dto.PostDto;
import com.andile.blogapi.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for PostController
 */
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPosts_ShouldReturnPostList() throws Exception {
        // Given
        PostDto post1 = new PostDto();
        post1.setId(1L);
        post1.setTitle("First Post");
        post1.setContent("First content");
        post1.setAuthorId(1L);
        post1.setCreatedAt(LocalDateTime.now());

        PostDto post2 = new PostDto();
        post2.setId(2L);
        post2.setTitle("Second Post");
        post2.setContent("Second content");
        post2.setAuthorId(1L);
        post2.setCreatedAt(LocalDateTime.now());

        List<PostDto> posts = Arrays.asList(post1, post2);
        when(postService.getAllPosts()).thenReturn(posts);

        // When & Then
        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("First Post"))
                .andExpect(jsonPath("$[1].title").value("Second Post"));
    }

    @Test
    void getPostById_ShouldReturnPost() throws Exception {
        // Given
        PostDto post = new PostDto();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setContent("Test content");
        post.setAuthorId(1L);

        when(postService.getPostById(1L)).thenReturn(post);

        // When & Then
        mockMvc.perform(get("/api/v1/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    void createPost_ShouldCreatePost() throws Exception {
        // Given
        PostDto inputPost = new PostDto();
        inputPost.setTitle("New Post");
        inputPost.setContent("New content");
        inputPost.setAuthorId(1L);

        PostDto createdPost = new PostDto();
        createdPost.setId(1L);
        createdPost.setTitle("New Post");
        createdPost.setContent("New content");
        createdPost.setAuthorId(1L);
        createdPost.setCreatedAt(LocalDateTime.now());

        when(postService.createPost(any(PostDto.class))).thenReturn(createdPost);

        // When & Then
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputPost)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Post"));
    }

    @Test
    void updatePost_ShouldUpdatePost() throws Exception {
        // Given
        PostDto updatePost = new PostDto();
        updatePost.setTitle("Updated Post");
        updatePost.setContent("Updated content");
        updatePost.setAuthorId(1L);

        PostDto updatedPost = new PostDto();
        updatedPost.setId(1L);
        updatedPost.setTitle("Updated Post");
        updatedPost.setContent("Updated content");
        updatedPost.setAuthorId(1L);

        when(postService.updatePost(eq(1L), any(PostDto.class))).thenReturn(updatedPost);

        // When & Then
        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Post"));
    }

    @Test
    void deletePost_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/posts/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createPost_WithInvalidData_ShouldReturnValidationError() throws Exception {
        // Given - PostDto with missing required fields
        PostDto invalidPost = new PostDto();
        // Missing title and content (required @NotBlank fields)
        invalidPost.setAuthorId(1L);

        // When & Then
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Validation failed:")));
    }
}