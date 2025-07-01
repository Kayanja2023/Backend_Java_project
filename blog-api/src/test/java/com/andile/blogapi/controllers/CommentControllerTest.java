package com.andile.blogapi.controllers;

import com.andile.blogapi.dto.CommentDto;
import com.andile.blogapi.service.CommentService;
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
 * Integration tests for CommentController
 */
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCommentsByPost_ShouldReturnCommentList() throws Exception {
        // Given
        CommentDto comment1 = new CommentDto();
        comment1.setId(1L);
        comment1.setContent("First comment");
        comment1.setAuthorId(1L);
        comment1.setPostId(1L);
        comment1.setCreatedAt(LocalDateTime.now());

        CommentDto comment2 = new CommentDto();
        comment2.setId(2L);
        comment2.setContent("Second comment");
        comment2.setAuthorId(2L);
        comment2.setPostId(1L);
        comment2.setCreatedAt(LocalDateTime.now());

        List<CommentDto> comments = Arrays.asList(comment1, comment2);
        when(commentService.getCommentsByPost(1L)).thenReturn(comments);

        // When & Then
        mockMvc.perform(get("/api/v1/comments/post/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("First comment"))
                .andExpect(jsonPath("$[1].content").value("Second comment"));
    }

    @Test
    void getCommentById_ShouldReturnComment() throws Exception {
        // Given
        CommentDto comment = new CommentDto();
        comment.setId(1L);
        comment.setContent("Test comment");
        comment.setAuthorId(1L);
        comment.setPostId(1L);

        when(commentService.getCommentById(1L)).thenReturn(comment);

        // When & Then
        mockMvc.perform(get("/api/v1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value("Test comment"))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.postId").value(1));
    }

    @Test
    void createComment_ShouldCreateComment() throws Exception {
        // Given
        CommentDto inputComment = new CommentDto();
        inputComment.setContent("New comment");
        inputComment.setAuthorId(1L);
        inputComment.setPostId(1L);

        CommentDto createdComment = new CommentDto();
        createdComment.setId(1L);
        createdComment.setContent("New comment");
        createdComment.setAuthorId(1L);
        createdComment.setPostId(1L);
        createdComment.setCreatedAt(LocalDateTime.now());

        when(commentService.createComment(any(CommentDto.class))).thenReturn(createdComment);

        // When & Then
        mockMvc.perform(post("/api/v1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputComment)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("New comment"));
    }

    @Test
    void updateComment_ShouldUpdateComment() throws Exception {
        // Given
        CommentDto updatedComment = new CommentDto();
        updatedComment.setId(1L);
        updatedComment.setContent("Updated comment");
        updatedComment.setAuthorId(1L);
        updatedComment.setPostId(1L);

        when(commentService.updateComment(eq(1L), eq("Updated comment"))).thenReturn(updatedComment);

        // When & Then
        mockMvc.perform(put("/api/v1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Updated comment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated comment"));
    }

    @Test
    void deleteComment_ShouldReturnNoContent() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/comments/1"))
                .andExpect(status().isNoContent());
    }
}