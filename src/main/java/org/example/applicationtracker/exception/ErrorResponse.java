package org.example.applicationtracker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse extends RuntimeException{
    private int status;
    private String message;
    private LocalDateTime timeStamp;
}
