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
import javax.validation.Valid;
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
    public User create(@Valid @RequestBody User user) {
        log.debug(LoggingMessages.CREATE.toString(), user);
        return service.create(user);
    }

    @GetMapping("{id}")
    public User get(@PathVariable Long id) {
        log.debug(LoggingMessages.GET.toString(), id);
        return service.get(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.debug(LoggingMessages.GET_ALL.toString());
        return service.getAll();
    }

    @PatchMapping("{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        log.debug(LoggingMessages.UPDATE.toString(), user);
        return service.update(id, user);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.debug(LoggingMessages.DELETE.toString(), id);
        service.delete(id);
    }
}
