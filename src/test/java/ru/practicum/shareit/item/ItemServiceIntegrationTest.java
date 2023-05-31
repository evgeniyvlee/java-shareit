package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url = jdbc:h2:mem:test")
public class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    private final List<UserDto> users = new ArrayList<>();
    private final List<ItemDto> items = new ArrayList<>();
    private final List<CommentDto> comments = new ArrayList<>();
    private final List<BookingDto> bookings = new ArrayList<>();

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

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(1L);
        itemDto1.setName("Item 1");
        itemDto1.setDescription("Item 1 description");
        itemDto1.setAvailable(true);
        items.add(itemDto1);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setId(2L);
        itemDto2.setName("Item 2");
        itemDto2.setDescription("Item 2 description");
        itemDto2.setAvailable(true);
        items.add(itemDto2);

        CommentDto comment = new CommentDto();
        comment.setId(1L);
        comment.setText("Comment 1");
        comment.setAuthorName(user2.getName());
        comment.setCreated(LocalDateTime.now());
    }

    @Test
    public void createTest() {
        ItemDto expectedItem = items.get(0);
        UserDto user = users.get(0);
        userService.create(user);
        ItemDto actualItem = itemService.create(expectedItem, user.getId());

        Assertions.assertEquals(expectedItem.getId(), actualItem.getId());
        Assertions.assertEquals(expectedItem.getName(), actualItem.getName());
        Assertions.assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        Assertions.assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());
    }

    @Test
    public void createWithNoExistUserTest() {
        Exception exception = Assertions.assertThrows(DataNotFoundException.class,
                () -> itemService.create(items.get(0), 3L));
        Assertions.assertEquals(ExceptionMessages.DATA_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void updateTest() {
        UserDto user = users.get(0);
        userService.create(user);

        ItemDto expectedItem = new ItemDto();
        expectedItem.setId(items.get(0).getId());
        expectedItem.setName(items.get(0).getName());
        expectedItem.setDescription("Item 1 updated description");
        expectedItem.setAvailable(items.get(0).getAvailable());
        expectedItem.setOwnerId(user.getId());

        itemService.create(items.get(0), user.getId());

        ItemDto actualItem = itemService.update(expectedItem.getId(), expectedItem, user.getId());
        Assertions.assertEquals(expectedItem.getId(), actualItem.getId());
        Assertions.assertEquals(expectedItem.getName(), actualItem.getName());
        Assertions.assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        Assertions.assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());
    }

    @Test
    public void updateWithNoAccessTest() {
        UserDto user = users.get(0);
        userService.create(user);

        ItemDto expectedItem = new ItemDto();
        expectedItem.setId(items.get(0).getId());
        expectedItem.setName(items.get(0).getName());
        expectedItem.setDescription("Item 1 updated description");
        expectedItem.setAvailable(items.get(0).getAvailable());
        expectedItem.setOwnerId(user.getId());

        itemService.create(items.get(0), user.getId());

        Assertions.assertThrows(
                ForbiddenException.class, () -> itemService.update(expectedItem.getId(), expectedItem, 3L)
        );
    }

    @Test
    public void getByIdTest() {
        ItemDto expectedItem = items.get(0);
        UserDto user = users.get(0);
        userService.create(user);
        itemService.create(expectedItem, user.getId());

        ItemDto actualItem = itemService.get(expectedItem.getId(), user.getId());
        Assertions.assertEquals(expectedItem.getId(), actualItem.getId());
        Assertions.assertEquals(expectedItem.getName(), actualItem.getName());
        Assertions.assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        Assertions.assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());
    }

    @Test
    public void getByWrongIdTest() {
        Assertions.assertThrows(DataNotFoundException.class, () -> itemService.get(2L, 1L));
    }

    @Test
    public void deleteTest() {
        ItemDto expectedItem = items.get(0);
        UserDto user = users.get(0);
        userService.create(user);
        ItemDto actualItem = itemService.create(expectedItem, user.getId());

        Assertions.assertEquals(expectedItem.getId(), actualItem.getId());
        Assertions.assertEquals(expectedItem.getName(), actualItem.getName());
        Assertions.assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        Assertions.assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());

        itemService.delete(actualItem.getId());
        Assertions.assertThrows(DataNotFoundException.class, () -> itemService.get(actualItem.getId(), user.getId()));
    }

    @Test
    public void getByOwnerTest() {
        UserDto owner1 = users.get(0);
        userService.create(owner1);
        UserDto owner2 = users.get(0);
        userService.create(owner2);

        itemService.create(items.get(0), owner1.getId());
        itemService.create(items.get(1), owner2.getId());

        List<ItemDto> actualItems = itemService.getByOwner(owner2.getId(), 0, 1);
        Assertions.assertEquals(1, actualItems.size());
        ItemDto actualItem = actualItems.get(0);
        ItemDto expectedItem = items.get(0);
        Assertions.assertEquals(expectedItem.getId(), actualItem.getId());
        Assertions.assertEquals(expectedItem.getName(), actualItem.getName());
        Assertions.assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        Assertions.assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());
    }

    @Test
    public void searchTest() {
        UserDto owner1 = users.get(0);
        userService.create(owner1);
        UserDto owner2 = users.get(0);
        userService.create(owner2);

        itemService.create(items.get(0), owner1.getId());
        itemService.create(items.get(1), owner2.getId());

        List<ItemDto> actualItems = itemService.search(owner1.getId(), "1 DeScriPtioN", 0, 1);
        Assertions.assertEquals(1, actualItems.size());
        ItemDto actualItem = actualItems.get(0);
        ItemDto expectedItem = items.get(0);
        Assertions.assertEquals(expectedItem.getId(), actualItem.getId());
        Assertions.assertEquals(expectedItem.getName(), actualItem.getName());
        Assertions.assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        Assertions.assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());
    }
}
