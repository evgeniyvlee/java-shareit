package ru.practicum.shareit.messages;

public interface ExceptionMessages {
    String DATA_ALREADY_EXIST = "Data is already exist";
    String DATA_NOT_FOUND = "Data not found";
    String USER_NAME_NOT_UNIQUE = "User name is not unique";
    String USER_EMAIL_NOT_UNIQUE = "User email is not unique";
    String INVALID_OWNER = "Not owner tries modify item";
    String ITEM_WITH_NO_OWNER = "Try to create item without owner";
}
