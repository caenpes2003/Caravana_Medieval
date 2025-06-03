package com.ejemplo.caravana.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ejemplo.caravana.exception.BusinessException;
import com.ejemplo.caravana.exception.ResourceNotFoundException;
import com.ejemplo.caravana.exception.VictoryException; 

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle400(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
            .body("Petición inválida: " + ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "))
            );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handle404(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handle422(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                             .body(ex.getMessage());
    }

    @ExceptionHandler(VictoryException.class)
    public ResponseEntity<String> handleVictory(VictoryException ex) {
        return ResponseEntity.ok(ex.getMessage());
}


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error inesperado: " + ex.getMessage());
    }
}