package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private final List<Item> items = new ArrayList<>();
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

        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Item 1 description");
        item1.setAvailable(true);
        item1.setOwner(user1);
        item1.setRequest(request1);
        items.add(itemRepository.save(item1));

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Item 2 description");
        item2.setAvailable(true);
        item2.setOwner(user2);
        item2.setRequest(request2);
        items.add(itemRepository.save(item2));
    }

    @AfterEach
    public void afterEach() {
        users.clear();
        requests.clear();
        items.clear();
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    public void findByOwnerIdTest() {
        Pageable pageable = PageRequest.of(0 / 1, 1);

        Assertions.assertEquals(List.of(items.get(0)), itemRepository.findByOwnerId(users.get(0).getId(), pageable));
        Assertions.assertEquals(List.of(items.get(1)), itemRepository.findByOwnerId(users.get(1).getId(), pageable));
    }

    @Test
    public void findNameOrDescriptionContainingTextTest() {
        Pageable pageable = PageRequest.of(0 / 1, 1);

        Assertions.assertEquals(List.of(items.get(0)),
                itemRepository.findNameOrDescriptionContainingText("ItEm 1", pageable));
        Assertions.assertEquals(List.of(items.get(1)),
                itemRepository.findNameOrDescriptionContainingText("ItEm 2", pageable));
    }

    @Test
    public void findByRequestIdInTest() {
        Assertions.assertEquals(List.of(items.get(0)),
                itemRepository.findByRequestIdIn(List.of(requests.get(0).getId())));
        Assertions.assertEquals(List.of(items.get(1)),
                itemRepository.findByRequestIdIn(List.of(requests.get(1).getId())));
    }
}
