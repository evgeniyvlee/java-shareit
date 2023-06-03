package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url = jdbc:h2:mem:test")
public class UserServiceIntegrationTest {

    @Autowired
    private final UserService service;

    private final List<UserDto> users = new ArrayList<>();

    @BeforeEach
    @Sql({"schema.sql"})
    public void beforeEach() {
        users.clear();
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@yandex.ru");
        users.add(user1);
        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@yandex.ru");
        users.add(user2);
        UserDto user3 = new UserDto();
        user3.setId(3L);
        user3.setName("User 3");
        user3.setEmail("user3@yandex.ru");
        users.add(user3);

        for (UserDto user : users) {
            service.create(user);
        }
    }

    @Test
    public void createUserTest() {
        UserDto user = users.get(0);
        UserDto userFromRepository = service.get(user.getId());
        Assertions.assertEquals(user, userFromRepository);
    }

    @Test
    public void createUserWithDuplicateEmailTest() {
        UserDto existUser = users.get(0);
        UserDto newUser = new UserDto();
        newUser.setId(4L);
        newUser.setName("User 4");
        newUser.setEmail(existUser.getEmail());
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> service.create(newUser));
    }

    @Test
    public void updateUserFailEmailTest() {
        UserDto user = users.get(0);
        user.setEmail(users.get(1).getEmail());
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> service.update(user.getId(), user));
    }

    @Test
    public void updateUserTest() {
        UserDto user = users.get(0);
        String newName = "User 1 new name";
        user.setName(newName);
        UserDto updatedUser = service.update(user.getId(), user);
        Assertions.assertEquals(user, updatedUser);
    }

    @Test
    public void updateNoExistUserTest() {
        UserDto user = new UserDto();
        user.setId(4L);
        user.setName("User 4");
        user.setEmail("user4@yandex.ru");
        Assertions.assertThrows(DataNotFoundException.class, () -> service.update(user.getId(), user));
    }

    @Test
    public void getAllUsersTest() {
        List<UserDto> usersFromDb = service.getAll();
        Assertions.assertEquals(users.size(), usersFromDb.size());
        for (int i = 0; i < usersFromDb.size(); i++) {
            Assertions.assertEquals(users.get(i), usersFromDb.get(i));
        }
    }

    @Test
    public void deleteUser() {
        service.delete(users.get(2).getId());
        users.remove(2);
        List<UserDto> usersFromDb = service.getAll();
        Assertions.assertEquals(users.size(), usersFromDb.size());
        for (int i = 0; i < usersFromDb.size(); i++) {
            Assertions.assertEquals(users.get(i), usersFromDb.get(i));
        }
    }
}
