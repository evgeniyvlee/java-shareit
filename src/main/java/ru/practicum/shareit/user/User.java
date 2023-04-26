package ru.practicum.shareit.user;

import ru.practicum.shareit.data.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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