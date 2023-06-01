package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private final List<Item> items = new ArrayList<>();
    private final List<ItemRequest> requests = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

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
        item1.setOwner(user2);
        item1.setRequest(request1);
        items.add(itemRepository.save(item1));

        Item item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Item 2 description");
        item2.setAvailable(true);
        item2.setOwner(user1);
        item2.setRequest(request2);
        items.add(itemRepository.save(item2));

        Booking booking1 = new Booking();
        booking1.setBooker(user1);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(3));
        bookings.add(bookingRepository.save(booking1));

        Booking booking2 = new Booking();
        booking2.setBooker(user2);
        booking2.setItem(item2);
        booking2.setStart(LocalDateTime.now().plusDays(4));
        booking2.setEnd(LocalDateTime.now().plusDays(7));
        bookings.add(bookingRepository.save(booking2));
    }

    @AfterEach
    public void afterEach() {
        users.clear();
        requests.clear();
        items.clear();
        bookings.clear();
        userRepository.deleteAll();
        requestRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void findAllByBookerIdAndStartBeforeAndEndAfterTest() {
        Pageable pageable = PageRequest.of(0 / 1, 1);

        Assertions.assertEquals(List.of(bookings.get(0)),
                bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(users.get(0).getId(),
                        LocalDateTime.now().plusDays(2), pageable));

        Assertions.assertEquals(List.of(bookings.get(1)),
                bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(users.get(1).getId(),
                        LocalDateTime.now().plusDays(5), pageable));
    }

    @Test
    public void findAllByItemOwnerIdAndStartBeforeAndEndAfterTest() {
        Pageable pageable = PageRequest.of(0 / 1, 1);

        Assertions.assertEquals(List.of(bookings.get(0)),
            bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(users.get(1).getId(),
                    LocalDateTime.now().plusDays(2), pageable));

        Assertions.assertEquals(List.of(bookings.get(1)),
                bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(users.get(0).getId(),
                        LocalDateTime.now().plusDays(5), pageable));
    }
}
