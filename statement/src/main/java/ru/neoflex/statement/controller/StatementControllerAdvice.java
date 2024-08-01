package ru.neoflex.statement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.neoflex.calculator.dto.exception.CreditDeniedException;
import ru.neoflex.calculator.dto.exception.PrescoringFailedException;
import ru.neoflex.statement.exception.DealException;

@ControllerAdvice
public class StatementControllerAdvice {

    @ExceptionHandler(PrescoringFailedException.class)
    public ResponseEntity<String> handlePrescoringFailedExceptions(PrescoringFailedException e) {
        return ResponseEntity.badRequest().body(String.format("PrescoringFailedException: %s", e.getMessage()));
    }

    @ExceptionHandler(CreditDeniedException.class)
    public ResponseEntity<String> handleCreditDeniedExceptions(CreditDeniedException e) {
        return ResponseEntity.badRequest().body(String.format("CreditDeniedException: %s", e.getMessage()));
    }

    @ExceptionHandler(DealException.class)
    public ResponseEntity<String> handleDealException(DealException e) {
        return ResponseEntity.badRequest().body(String.format("DealException: %s", e.getMessage()));
    }
}
