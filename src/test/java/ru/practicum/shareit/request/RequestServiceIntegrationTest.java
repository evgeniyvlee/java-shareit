package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url = jdbc:h2:mem:test")
public class RequestServiceIntegrationTest {

    @Autowired
    private final RequestService requestService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    private final List<ItemRequestDto> requests = new ArrayList<>();

    private final List<UserDto> users = new ArrayList<>();

    @BeforeEach
    @Sql({"schema.sql"})
    public void beforeEach() {
        requests.clear();
        ItemRequestDto request1 = new ItemRequestDto();
        request1.setId(1L);
        request1.setDescription("Item request 1 description");
        request1.setCreated(LocalDateTime.now());
        requests.add(request1);
        ItemRequestDto request2 = new ItemRequestDto();
        request2.setId(2L);
        request2.setDescription("Item request 2 description");
        request2.setCreated(LocalDateTime.now());
        requests.add(request2);

        users.clear();
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@yandex.ru");
        users.add(user1);
        userService.create(user1);
        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@yandex.ru");
        users.add(user1);
        userService.create(user2);
    }

    @Test
    public void createRequestTest() {
        UserDto user = users.get(0);
        ItemRequestDto expectedRequest = requests.get(0);
        ItemRequestDto actualRequest = requestService.create(expectedRequest, user.getId());

        Assertions.assertEquals(expectedRequest.getId(), actualRequest.getId());
        Assertions.assertEquals(expectedRequest.getDescription(), actualRequest.getDescription());
    }

    @Test
    public void getRequestTest() {
        UserDto user = users.get(0);
        ItemRequestDto expectedRequest = requests.get(0);
        requestService.create(expectedRequest, user.getId());
        ItemRequestDto actualRequest = requestService.get(expectedRequest.getId(), user.getId());
        Assertions.assertEquals(expectedRequest.getId(), actualRequest.getId());
        Assertions.assertEquals(expectedRequest.getDescription(), actualRequest.getDescription());
    }

    @Test
    public void getAllByRequesterIdTest() {
        UserDto requester = users.get(0);
        UserDto otherUser = users.get(1);
        ItemRequestDto expectedRequest = requests.get(0);
        ItemRequestDto otherRequest = requests.get(1);
        requestService.create(expectedRequest, requester.getId());
        requestService.create(otherRequest, otherUser.getId());

        List<ItemRequestDto> actualRequests = requestService.getAllByRequesterId(requester.getId(), 0, 1);
        ItemRequestDto actualRequest = actualRequests.get(0);
        Assertions.assertEquals(1, actualRequests.size());
        Assertions.assertEquals(expectedRequest.getId(), actualRequest.getId());
        Assertions.assertEquals(expectedRequest.getDescription(), actualRequest.getDescription());
    }

    @Test
    public void getAllOtherUsersTest() {
        UserDto requester = users.get(0);
        UserDto otherUser = users.get(1);
        ItemRequestDto request = requests.get(0);
        ItemRequestDto otherRequest = requests.get(1);
        requestService.create(request, requester.getId());
        requestService.create(otherRequest, otherUser.getId());

        List<ItemRequestDto> actualRequests = requestService.getAllByRequesterId(otherUser.getId(), 0, 1);
        ItemRequestDto actualRequest = actualRequests.get(0);
        Assertions.assertEquals(1, actualRequests.size());
        Assertions.assertEquals(request.getId(), actualRequest.getId());
        Assertions.assertEquals(request.getDescription(), actualRequest.getDescription());

    }
}
