package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.data.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * User DTO
 * @author Evgeniy Lee
 */
@lombok.Data
public class UserDto extends Data {
    // User name
    @NotBlank
    private String name;
    // User email
    @Email
    @NotBlank
    private String email;
}
