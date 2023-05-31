package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url = jdbc:h2:mem:test")
public class BookingServiceIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingService bookingService;

    private final List<User> users = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    @BeforeEach
    @Sql({"schema.sql"})
    public void beforeEach() {
        users.clear();
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@yandex.ru");
        users.add(user1);
        userRepository.save(user1);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@yandex.ru");
        users.add(user2);
        userRepository.save(user2);

        items.clear();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Item description 1");
        item1.setAvailable(true);
        item1.setOwner(user1);
        items.add(item1);
        itemRepository.save(item1);

        bookings.clear();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LocalDateTime.now());
        booking1.setEnd(LocalDateTime.now().plusDays(7));
        booking1.setItem(item1);
        booking1.setBooker(user2);
        bookings.add(booking1);
        bookingRepository.save(booking1);
    }

    @Test
    public void createBookingTest() {
        Booking booking = bookings.get(0);
        User user = users.get(1);
        BookingDto actualBooking = bookingService.create(BookingMapper.toBriefBookingDto(booking), user.getId());
        BookingDto expectedBooking = BookingMapper.toBookingDto(booking);

        Assertions.assertEquals(expectedBooking, actualBooking);
    }

    @Test
    public void approveBookingTest() {
        Booking booking = bookings.get(0);
        bookingRepository.save(booking);
        BookingDto actualBooking = bookingService.approve(booking.getId(), users.get(0).getId(), true);
        BookingDto expectedBooking = BookingMapper.toBookingDto(booking);
        expectedBooking.setStatus(BookingStatus.APPROVED);

        Assertions.assertEquals(expectedBooking, actualBooking);
    }

    @Test
    public void approveBookingTestFailed() {
        Booking booking = bookings.get(0);
        bookingRepository.save(booking);
        ForbiddenException exception =
                Assertions.assertThrows(ForbiddenException.class,
                        () -> bookingService.approve(booking.getId(), users.get(1).getId(), true));

        Assertions.assertEquals(ExceptionMessages.ACCESS_DENIED, exception.getMessage());
    }

    @Test
    public void getByBookerIdTest() {
        User booker = users.get(1);

        final Booking booking = bookings.get(0);
        bookingRepository.save(booking);
        List<BookingDto> expectedBookings = BookingMapper.toBookingDtoList(bookings);
        List<BookingDto> actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.ALL, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.WAITING, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);


        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        expectedBookings = BookingMapper.toBookingDtoList(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.REJECTED, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.CURRENT, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        booking.setStart(LocalDateTime.now().minusDays(7));
        booking.setEnd(LocalDateTime.now().minusDays(5));
        bookingRepository.save(booking);
        expectedBookings = BookingMapper.toBookingDtoList(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.PAST, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        booking.setStart(LocalDateTime.now().plusDays(5));
        booking.setEnd(LocalDateTime.now().plusDays(7));
        bookingRepository.save(booking);
        expectedBookings = BookingMapper.toBookingDtoList(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.FUTURE, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void getByOwnerIdTest() {
        User owner = users.get(0);
        final Booking booking = bookings.get(0);
        bookingRepository.save(booking);
        List<BookingDto> expectedBookings = BookingMapper.toBookingDtoList(bookings);
        List<BookingDto> actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.ALL, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.WAITING, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);


        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        expectedBookings = BookingMapper.toBookingDtoList(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.REJECTED, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.CURRENT, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        booking.setStart(LocalDateTime.now().minusDays(7));
        booking.setEnd(LocalDateTime.now().minusDays(5));
        bookingRepository.save(booking);
        expectedBookings = BookingMapper.toBookingDtoList(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.PAST, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        booking.setStart(LocalDateTime.now().plusDays(5));
        booking.setEnd(LocalDateTime.now().plusDays(7));
        bookingRepository.save(booking);
        expectedBookings = BookingMapper.toBookingDtoList(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.FUTURE, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);
    }
}
