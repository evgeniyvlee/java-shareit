package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User service implementation
 * @author Evgeniy Lee
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    public UserServiceImpl(@Qualifier("memoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User create(final User user) {
        validate(user);
        return storage.create(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User get(final Long id) {
        User user = storage.get(id);
        if (user == null) {
            throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        return storage.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User update(final Long id, final User user) {
        User existUser = get(id);
        fillUserInfoForUpdate(existUser, user);
        validate(user);
        return storage.update(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        User user = get(id);
        storage.delete(user.getId());
    }

    private boolean validate(final User user) {
        List<User> usersWithName = storage.getByName(user.getName()).stream()
                .filter(i -> !i.getId().equals(user.getId()))
                .collect(Collectors.toList());
        List<User> usersWithEmail = storage.getByEmail(user.getEmail()).stream()
                .filter(i -> !i.getId().equals(user.getId()))
                .collect(Collectors.toList());;
        if (usersWithName.size() > 0 || usersWithEmail.size() > 0) {
            throw new DataAlreadyExistException(ExceptionMessages.DATA_ALREADY_EXIST);
        }
        return true;
    }

    private void fillUserInfoForUpdate(User existUser, User user) {
        user.setId(existUser.getId());
        if (user.getName() == null) {
            user.setName(existUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(existUser.getEmail());
        }
    }
}
