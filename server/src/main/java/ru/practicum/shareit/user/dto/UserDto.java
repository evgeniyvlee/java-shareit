package ru.practicum.shareit.user.dto;

import lombok.Data;

/**
 * User DTO
 * @author Evgeniy Lee
 */
@Data
public class UserDto {
    // User ID
    private Long id;
    // User name
    private String name;
    // User email
    private String email;
}
