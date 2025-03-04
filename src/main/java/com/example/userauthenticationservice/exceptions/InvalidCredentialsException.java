package com.example.userauthenticationservice.exceptions;

public class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
}
