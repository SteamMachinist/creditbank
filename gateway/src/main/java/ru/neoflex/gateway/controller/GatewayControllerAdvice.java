package ru.neoflex.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GatewayControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlePrescoringFailedExceptions(RuntimeException e) {
        return ResponseEntity.badRequest().body(String.format("RuntimeException: %s", e.getMessage()));
    }
}
