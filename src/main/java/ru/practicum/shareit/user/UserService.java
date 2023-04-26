package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * User service interface for CRUD operations
 * @author Evgeniy Lee
 */
public interface UserService {
    /**
     * Create user
     * @param user user
     * @return created user
     */
    UserDto create(UserDto user);

    /**
     * Get user by ID
     * @param id user ID
     * @return found user
     */
    UserDto get(Long id);

    /**
     * Get all users
     * @return users
     */
    List<UserDto> getAll();

    /**
     * Update user
     * @param id user ID
     * @param user updated user
     * @return user
     */
    UserDto update(Long id, UserDto user);

    /**
     * Delete user b ID
     * @param id user ID
     */
    void delete(Long id);
}
