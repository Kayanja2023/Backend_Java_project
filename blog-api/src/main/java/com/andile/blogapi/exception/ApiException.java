package com.andile.blogapi.exception;


 // Simple exception for all API errors

public class ApiException extends RuntimeException {
    
    public ApiException(String message) {
        super(message);
    }
}