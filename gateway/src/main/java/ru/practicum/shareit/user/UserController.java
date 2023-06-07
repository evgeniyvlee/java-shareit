package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LoggingMessages;
import ru.practicum.shareit.user.dto.UserDto;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.debug(LoggingMessages.CREATE.toString(), userDto);
        return userClient.create(userDto);
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        log.debug(LoggingMessages.GET.toString(), userId);
        return userClient.get(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.debug(LoggingMessages.GET_ALL.toString());
        return userClient.getAll();
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.debug(LoggingMessages.UPDATE.toString(), userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        log.debug(LoggingMessages.DELETE.toString(), userId);
        return userClient.delete(userId);
    }
}
