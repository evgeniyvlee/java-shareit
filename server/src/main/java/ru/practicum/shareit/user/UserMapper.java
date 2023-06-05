package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class for converting user
 * @author Evgeniy Lee
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    /**
     * Converts user to user DTO object
     * @param user user info
     * @return user DTO object
     */
    public static UserDto toUserDto(final User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    /**
     * Converts user Dto to user
     * @param userDto user DTO
     * @return user
     */
    public static User toUser(final UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    /**
     * Convert list of users to list of users DTO
     * @param users list of users
     * @return list of users DTO
     */
    public static List<UserDto> toUserDtoList(final List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        if (users != null)
            users.stream().forEach(u -> usersDto.add(UserMapper.toUserDto(u)));
        return usersDto;
    }
}
