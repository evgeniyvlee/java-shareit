package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.data.InMemoryDataStorage;
import java.util.HashSet;
import java.util.Set;

/**
 * User storage
 * @author Evgeniy Lee
 */
@Component
@Qualifier("memoryUserStorage")
public class InMemoryUserStorage extends InMemoryDataStorage<User> implements UserStorage {
    // Name set
    private final Set<String> names = new HashSet<>();
    // Email set
    private final Set<String> emails = new HashSet<>();

    @Override
    public User create(User user) {
        User newUser = super.create(user);
        names.add(user.getName());
        emails.add(user.getEmail());
        return newUser;
    }

    @Override
    public User update(User user) {
        User oldUser = super.get(user.getId());
        User updatedUser = super.update(user);
        updateUserNameEmailSet(names, oldUser.getName(), updatedUser.getName());
        updateUserNameEmailSet(emails, oldUser.getEmail(), updatedUser.getEmail());
        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        User oldUser = super.get(id);
        names.remove(oldUser.getName());
        emails.remove(oldUser.getEmail());
        super.delete(id);

    }

    @Override
    public boolean isExist(String name, String email) {
        if (((name != null) && names.contains(name)) || ((email != null) && emails.contains(email)))
            return true;
        return false;
    }

    private void updateUserNameEmailSet(final Set<String> userSet, final String oldInfo, final String newInfo) {
        if (!oldInfo.equals(newInfo)) {
            userSet.remove(oldInfo);
            userSet.add(newInfo);
        }
    }
}
