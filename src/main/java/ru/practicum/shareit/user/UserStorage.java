package ru.practicum.shareit.user;

import ru.practicum.shareit.data.DataStorage;

/**
 * User storage interface
 * @author Evgeniy Lee
 */
public interface UserStorage extends DataStorage<User> {
    /**
     * Check is user with name or email exists in storage
     * @param name user name
     * @param email user email
     * @return is exist?
     */
    boolean isExist(String name, String email);
}