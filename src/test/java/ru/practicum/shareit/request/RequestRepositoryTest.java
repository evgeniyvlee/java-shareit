package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class RequestRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    private final List<ItemRequest> requests = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@yandex.ru");
        users.add(userRepository.save(user1));

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@yandex.ru");
        users.add(userRepository.save(user2));

        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Request 1 description");
        request1.setCreated(LocalDateTime.now());
        request1.setRequester(user1);
        requests.add(requestRepository.save(request1));

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Request 2 description");
        request2.setCreated(LocalDateTime.now());
        request2.setRequester(user2);
        requests.add(requestRepository.save(request2));
    }

    @AfterEach
    public void afterEach() {
        users.clear();
        requests.clear();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    public void findAllByRequesterIdTest() {
        Pageable pageable = PageRequest.of(0 / 1, 1);

        Assertions.assertEquals(List.of(requests.get(0)),
                requestRepository.findAllByRequesterId(users.get(0).getId(), pageable));
        Assertions.assertEquals(List.of(requests.get(1)),
                requestRepository.findAllByRequesterId(users.get(1).getId(), pageable));
    }

    @Test
    public void findAllByRequesterIdNotTest() {
        Pageable pageable = PageRequest.of(0 / 1, 1);

        Assertions.assertEquals(List.of(requests.get(0)),
                requestRepository.findAllByRequesterIdNot(users.get(1).getId(), pageable));
        Assertions.assertEquals(List.of(requests.get(1)),
                requestRepository.findAllByRequesterIdNot(users.get(0).getId(), pageable));
    }
}
