package ru.practicum.shareit.messages;

public enum LoggingMessages {
    CREATE("Creating data {}"),
    GET("Getting data with id {}"),
    GET_ALL("Getting all data"),
    UPDATE("Updating data {}"),
    DELETE("Deleting data with id {}"),
    GET_ITEMS_BY_OWNER_ID("Getting items by owner ID"),
    SEARCH_ITEMS_BY_TEXT("Searching items by text in name or description");

    private String message;

    private LoggingMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
