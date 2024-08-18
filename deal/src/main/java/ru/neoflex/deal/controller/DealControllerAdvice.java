package ru.neoflex.deal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.neoflex.common.exception.CreditDeniedException;
import ru.neoflex.common.exception.PrescoringFailedException;
import ru.neoflex.common.exception.SesException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class DealControllerAdvice {

    @ExceptionHandler(PrescoringFailedException.class)
    public ResponseEntity<String> handlePrescoringFailedExceptions(PrescoringFailedException e) {
        return ResponseEntity.badRequest().body(String.format("PrescoringFailedException: %s", e.getMessage()));
    }

    @ExceptionHandler(CreditDeniedException.class)
    public ResponseEntity<String> handleCreditDeniedExceptions(CreditDeniedException e) {
        return ResponseEntity.badRequest().body(String.format("CreditDeniedException: %s", e.getMessage()));
    }

    @ExceptionHandler(SesException.class)
    public ResponseEntity<String> handleSesExceptions(CreditDeniedException e) {
        return ResponseEntity.badRequest().body(String.format("SesException: %s", e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(String.format("EntityNotFoundException: %s", e.getMessage()));
    }
}
