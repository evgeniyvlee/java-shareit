package ru.practicum.shareit.user.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * User DTO
 * @author Evgeniy Lee
 */
@Data
public class UserDto {
    // User ID
    private Long id;
    // User name
    @NotBlank
    private String name;
    // User email
    @Email
    @NotBlank
    private String email;
}
