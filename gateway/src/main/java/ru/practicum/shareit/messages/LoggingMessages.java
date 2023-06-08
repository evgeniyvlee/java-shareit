package ru.practicum.shareit.messages;

public enum LoggingMessages {
    CREATE("Creating data {}"),
    GET("Getting data with id {}"),
    GET_ALL("Getting all data"),
    UPDATE("Updating data {}"),
    DELETE("Deleting data with id {}"),
    GET_ITEMS_BY_OWNER_ID("Getting items by owner ID"),
    SEARCH_ITEMS_BY_TEXT("Searching items by text in name or description"),
    POST_COMMENT("Post comment to item with id = {}, text = {}"),
    APPROVE_BOOKING("Approve booking with id {}"),
    GET_BOOKINGS_BY_USER_ID("Getting bookings by user id {}"),
    GET_BOOKINGS_BY_OWNER_ID("Getting bookings by owner id {}");

    private String message;

    private LoggingMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
