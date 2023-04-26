package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

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
    public UserDto create(final UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        validate(user);
        return UserMapper.toUserDto(storage.create(user));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto get(final Long id) {
        User user = storage.get(id);
        if (user == null) {
            throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        }
        return UserMapper.toUserDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDto> getAll() {
        return UserMapper.toUsersDto(storage.getAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto update(final Long id, final UserDto userDto) {
        userDto.setId(id);
        User existUser = UserMapper.toUser(get(id));
        User user = UserMapper.toUser(userDto);
        validate(user);
        fillUserInfoForUpdate(existUser, user);
        return UserMapper.toUserDto(storage.update(user));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        UserDto user = get(id);
        storage.delete(user.getId());
    }

    private boolean validate(final User user) {
        if (storage.isExist(user.getName(), user.getEmail())) {
            Long userId = user.getId();
            if (userId == null) {
                throw new DataAlreadyExistException(ExceptionMessages.DATA_ALREADY_EXIST);
            } else {
                String userName = user.getName();
                String userEmail = user.getEmail();
                if ((userName != null && !storage.get(userId).getName().equals(userName)) ||
                        (userEmail != null && !storage.get(userId).getEmail().equals(userEmail)))
                {
                    throw new DataAlreadyExistException(ExceptionMessages.DATA_ALREADY_EXIST);
                }
            }
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
