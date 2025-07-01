package com.andile.blogapi.repositories;

import com.andile.blogapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by email address
    Optional<User> findByEmail(String email);
    
    // Find user by username
    Optional<User> findByUsername(String username);
    
    // Check if email already exists
    boolean existsByEmail(String email);
    
    // Check if username already exists
    boolean existsByUsername(String username);
}