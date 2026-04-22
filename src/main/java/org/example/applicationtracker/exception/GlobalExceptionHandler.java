package org.example.applicationtracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericException(Exception ex){
        if (ex instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> applicationNotfoundException(ApplicationNotFoundException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
}
