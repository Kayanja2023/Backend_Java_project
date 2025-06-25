package com.andile.blogapi.service;

import com.andile.blogapi.dto.CreatePostDto;
import com.andile.blogapi.dto.PostDto;
import com.andile.blogapi.entity.Post;
import com.andile.blogapi.entity.User;
import com.andile.blogapi.repositories.PostRepository;
import com.andile.blogapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return convertToDto(post);
    }
    
    public List<PostDto> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<PostDto> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public PostDto createPost(CreatePostDto createPostDto) {
        User author = userRepository.findById(createPostDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + createPostDto.getAuthorId()));
        
        Post post = new Post();
        post.setTitle(createPostDto.getTitle());
        post.setContent(createPostDto.getContent());
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }
    
    public PostDto updatePost(Long id, CreatePostDto updatePostDto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        
        existingPost.setTitle(updatePostDto.getTitle());
        existingPost.setContent(updatePostDto.getContent());
        
        Post updatedPost = postRepository.save(existingPost);
        return convertToDto(updatedPost);
    }
    
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
    
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