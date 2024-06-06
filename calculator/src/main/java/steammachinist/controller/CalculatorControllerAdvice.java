package steammachinist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import steammachinist.dto.exception.CreditDeniedException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CalculatorControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CreditDeniedException.class)
    public ResponseEntity<String> handleCreditDeniedExceptions(CreditDeniedException e) {
        return ResponseEntity.badRequest().body(String.format("CreditDeniedException: %s", e.getMessage()));
    }

}
