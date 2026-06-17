package com.organization.app.config;

import com.organization.app.field.domain.exception.FieldException;
import com.organization.app.matching.domain.exception.MatchRequestException;
import com.organization.app.mentor.domain.exception.MentorProfileException;
import com.organization.app.qa.domain.exception.InstantQAException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FieldException.class)
    public ResponseEntity<Map<String, Object>> handleFieldException(FieldException e) {
        return ResponseEntity.status(e.getStatus())
                .body(Map.of(
                        "code", e.getCode(),
                        "message", e.getMessage(),
                        "status", e.getStatus()
                ));
    }

    @ExceptionHandler(MentorProfileException.class)
    public ResponseEntity<Map<String, Object>> handleMentorProfileException(MentorProfileException e) {
        return ResponseEntity.status(e.getStatus())
                .body(Map.of(
                        "code", e.getCode(),
                        "message", e.getMessage(),
                        "status", e.getStatus()
                ));
    }

    @ExceptionHandler(InstantQAException.class)
    public ResponseEntity<Map<String, Object>> handleInstantQAException(InstantQAException e) {
        return ResponseEntity.status(e.getStatus())
                .body(Map.of(
                        "code", e.getCode(),
                        "message", e.getMessage(),
                        "status", e.getStatus()
                ));
    }

    @ExceptionHandler(MatchRequestException.class)
    public ResponseEntity<Map<String, Object>> handleMatchRequestException(MatchRequestException e) {
        return ResponseEntity.status(e.getStatus())
                .body(Map.of(
                        "code", e.getCode(),
                        "message", e.getMessage(),
                        "status", e.getStatus()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        // Map domain validation errors to 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "code", e.getMessage(), // Assuming message is the code like FIELD_NAME_REQUIRED
                        "status", 400
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        // Handle @Valid errors
        String code = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "code", code,
                        "status", 400
                ));
    }
}
