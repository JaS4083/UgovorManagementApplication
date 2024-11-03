package com.omega.software.management.controller;

import com.omega.software.management.data.exception.BadRequestException;
import com.omega.software.management.data.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("BadCredentialsException: ", ex);
        return new ResponseEntity<>(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException: ", ex);
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        log.error("BadRequestException: ", ex);
        return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
    }
}
