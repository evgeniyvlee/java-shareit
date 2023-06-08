package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * User service implementation
 * @author Evgeniy Lee
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        User user = repository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto get(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = repository.findAll();
        return UserMapper.toUserDtoList(users);
    }

    @Transactional
    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
