package ru.neoflex.common.exception;

public class CreditDeniedException extends RuntimeException {

    public CreditDeniedException(String message) {
        super(message);
    }
}
