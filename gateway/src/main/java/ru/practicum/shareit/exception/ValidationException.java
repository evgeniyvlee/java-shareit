package ru.practicum.shareit.exception;

public class ValidationException extends RuntimeException {
    /**
     * Constructor
     * @param message exception message
     */
    public ValidationException(String message) {
        super(message);
    }
}

