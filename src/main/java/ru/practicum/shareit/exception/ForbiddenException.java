package ru.practicum.shareit.exception;

public class ForbiddenException extends RuntimeException {
    /**
     * Constructor
     * @param message exception message
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
