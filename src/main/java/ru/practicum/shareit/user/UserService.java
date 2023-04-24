package ru.practicum.shareit.user;

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
    User create(User user);

    /**
     * Get user by ID
     * @param id user ID
     * @return found user
     */
    User get(Long id);

    /**
     * Get all users
     * @return users
     */
    List<User> getAll();

    /**
     * Update user
     * @param id user ID
     * @param user updated user
     * @return user
     */
    User update(Long id, User user);

    /**
     * Delete user b ID
     * @param id user ID
     */
    void delete(Long id);
}
