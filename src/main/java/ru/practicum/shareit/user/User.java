package ru.practicum.shareit.user;

import ru.practicum.shareit.data.Data;

/**
 * User info
 * @author Evgeniy Lee
 */
@lombok.Data
public class User extends Data {
    // User name
    private String name;
    // User email
    private String email;
}