package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final List<User> users = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        users.clear();
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@yandex.ru");
        users.add(user1);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@yandex.ru");
        users.add(user2);

        items.clear();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Item description 1");
        item1.setAvailable(true);
        item1.setOwner(user1);
        items.add(item1);

        bookings.clear();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LocalDateTime.now());
        booking1.setEnd(LocalDateTime.now().plusDays(7));
        booking1.setItem(item1);
        booking1.setBooker(user2);
        bookings.add(booking1);
    }

    @Test
    public void createBookingTest() {
        Booking booking = bookings.get(0);
        User user = users.get(1);
        Item item = items.get(0);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        BookingDto actualBooking = bookingService.create(BookingMapper.toBriefBookingDto(booking), user.getId());
        BookingDto expectedBooking = BookingMapper.toBookingDto(booking);

        Assertions.assertEquals(expectedBooking, actualBooking);
    }

    @Test
    public void approveBookingTest() {
        Booking booking = bookings.get(0);
        User user = users.get(1);
        Item item = items.get(0);
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        BookingDto actualBooking = bookingService.approve(booking.getId(), users.get(0).getId(), true);
        BookingDto expectedBooking = BookingMapper.toBookingDto(booking);
        expectedBooking.setStatus(BookingStatus.APPROVED);

        Assertions.assertEquals(expectedBooking, actualBooking);
    }

    @Test
    public void getByBookerIdTest() {
        User booker = users.get(1);
        List<BookingDto> expectedBookings = BookingMapper.toBookingDtoList(bookings);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booker));
        Mockito
                .when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        List<BookingDto> actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.ALL, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByBookerIdAndStatus(Mockito.anyLong(),
                        Mockito.any(BookingStatus.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.WAITING, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByBookerIdAndStatus(Mockito.anyLong(),
                        Mockito.any(BookingStatus.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.REJECTED, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.CURRENT, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByBookerIdAndEndBefore(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.PAST, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByBookerIdAndStartAfter(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByBookerId(booker.getId(), BookingSearchStatus.FUTURE, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void getByOwnerIdTest() {
        User owner = users.get(0);
        List<BookingDto> expectedBookings = BookingMapper.toBookingDtoList(bookings);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        Mockito
                .when(bookingRepository.findAllByItemOwnerId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        List<BookingDto> actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.ALL, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByItemOwnerIdAndStatus(Mockito.anyLong(),
                        Mockito.any(BookingStatus.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.WAITING, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByItemOwnerIdAndStatus(Mockito.anyLong(),
                        Mockito.any(BookingStatus.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.REJECTED, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.CURRENT, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByItemOwnerIdAndEndBefore(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.PAST, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);

        Mockito
                .when(bookingRepository.findAllByItemOwnerIdAndStartAfter(Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(bookings);
        actualBookings =
                bookingService.getByOwnerId(owner.getId(), BookingSearchStatus.FUTURE, 0, 1);
        Assertions.assertEquals(expectedBookings, actualBookings);
    }
}