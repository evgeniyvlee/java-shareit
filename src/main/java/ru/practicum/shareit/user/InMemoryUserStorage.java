package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.data.InMemoryDataStorage;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User storage
 * @author Evgeniy Lee
 */
@Component
@Qualifier("memoryUserStorage")
public class InMemoryUserStorage extends InMemoryDataStorage<User> implements UserStorage {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getByName(String name) {
        List<User> users = storage.values().stream()
                .filter(u -> name.matches(u.getName()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(users);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getByEmail(String email) {
        List<User> users = storage.values().stream()
                .filter(u -> email.matches(u.getEmail()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(users);
    }
}
