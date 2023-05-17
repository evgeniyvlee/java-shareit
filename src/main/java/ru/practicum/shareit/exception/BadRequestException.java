package ru.practicum.shareit.exception;

public class BadRequestException extends RuntimeException {
    /**
     * Constructor
     * @param message exception message
     */
    public BadRequestException(String message) {
        super(message);
    }
}
