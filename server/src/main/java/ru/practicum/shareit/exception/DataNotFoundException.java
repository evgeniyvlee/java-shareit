package ru.practicum.shareit.exception;

public class DataNotFoundException extends RuntimeException {
    /**
     * Constructor
     * @param message exception message
     */
    public DataNotFoundException(String message) {
        super(message);
    }
}
