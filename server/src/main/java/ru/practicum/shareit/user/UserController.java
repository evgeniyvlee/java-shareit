package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.messages.LoggingMessages;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

/**
 * User controller
 * @author Evgeniy Lee
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDto create(@RequestBody UserDto user) {
        log.debug(LoggingMessages.CREATE.toString(), user);
        return service.create(user);
    }

    @GetMapping("{userId}")
    public UserDto get(@PathVariable Long userId) {
        log.debug(LoggingMessages.GET.toString(), userId);
        return service.get(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.debug(LoggingMessages.GET_ALL.toString());
        return service.getAll();
    }

    @PatchMapping("{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto user) {
        log.debug(LoggingMessages.UPDATE.toString(), user);
        return service.update(userId, user);
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable Long userId) {
        log.debug(LoggingMessages.DELETE.toString(), userId);
        service.delete(userId);
    }
}
