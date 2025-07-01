package com.andile.blogapi.service;

import com.andile.blogapi.dto.PostDto;
import com.andile.blogapi.entity.Post;
import com.andile.blogapi.entity.User;
import com.andile.blogapi.exception.ApiException;
import com.andile.blogapi.repositories.PostRepository;
import com.andile.blogapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


 // Service layer for post operations
 
@Service
@RequiredArgsConstructor
public class PostService {
    
    // Repository dependencies
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    // Get all posts ordered by newest first
    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Get a single post by ID
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Post not found"));
        return convertToDto(post);
    }
    
    // Get all posts by a specific author
    public List<PostDto> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Search posts by title (case-insensitive)
    public List<PostDto> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Create a new post
    public PostDto createPost(PostDto postDto) {
        User author = userRepository.findById(postDto.getAuthorId())
                .orElseThrow(() -> new ApiException("Author not found"));
        
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }
    
    // Update existing post
    public PostDto updatePost(Long id, PostDto updatePostDto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ApiException("Post not found"));
        
        existingPost.setTitle(updatePostDto.getTitle());
        existingPost.setContent(updatePostDto.getContent());
        
        Post updatedPost = postRepository.save(existingPost);
        return convertToDto(updatedPost);
    }
    
    // Delete a post by ID
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ApiException("Post not found");
        }
        postRepository.deleteById(id);
    }
    
    // Convert Post entity to PostDto
    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        return dto;
    }
}