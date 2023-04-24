package ru.practicum.shareit.exception;

public class DataAlreadyExistException extends RuntimeException {
    /**
     * Constructor
     * @param message exception message
     */
    public DataAlreadyExistException(String message) {
        super(message);
    }
}

