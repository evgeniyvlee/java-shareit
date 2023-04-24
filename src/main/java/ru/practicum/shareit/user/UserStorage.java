package ru.practicum.shareit.user;

import ru.practicum.shareit.data.DataStorage;
import java.util.List;

/**
 * User storage interface
 * @author Evgeniy Lee
 */
public interface UserStorage extends DataStorage<User> {
    /**
     * Get user list by name
     * @param name user name
     * @return user list
     */
    List<User> getByName(String name);

    /**
     * Get user list by email
     * @param email user email
     * @return user list
     */
    List<User> getByEmail(String email);
}
