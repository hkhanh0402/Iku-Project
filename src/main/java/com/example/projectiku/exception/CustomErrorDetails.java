package com.example.projectiku.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private Object details;
}
