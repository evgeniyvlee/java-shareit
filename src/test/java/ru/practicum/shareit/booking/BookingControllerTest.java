package ru.practicum.shareit.booking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.LocalDateTimeAdapter;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BriefBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @MockBean
    private BookingService service;

    @Autowired
    private MockMvc mockMvc;

    private static Gson gson;

    @BeforeAll
    static void beforeAll() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

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
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(2));
        booking1.setItem(item1);
        booking1.setBooker(user2);
        bookings.add(booking1);
    }

    @Test
    public void createBookingTest() throws Exception {
        BookingDto bookingDto = BookingMapper.toBookingDto(bookings.get(0));
        User user = users.get(0);
        Mockito.when(service.create(Mockito.any(BriefBookingDto.class), Mockito.anyLong())).thenReturn(bookingDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(BookingMapper.toBriefBookingDto(bookings.get(0))))
                        .header(X_SHARER_USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void approveBookingTest() throws Exception {
        BookingDto bookingDto = BookingMapper.toBookingDto(bookings.get(0));
        User user = users.get(0);
        Mockito
                .when(service.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(BookingMapper.toBriefBookingDto(bookings.get(0))))
                        .header(X_SHARER_USER_ID, user.getId())
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void getBookingTest() throws Exception {
        BookingDto bookingDto = BookingMapper.toBookingDto(bookings.get(0));
        User user = users.get(0);
        Mockito
                .when(service.get(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", bookingDto.getId())
                        .header(X_SHARER_USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void getAllByUserTest() throws Exception {
        BookingDto bookingDto = BookingMapper.toBookingDto(bookings.get(0));
        User user = users.get(0);
        Mockito
                .when(service.getByBookerId(Mockito.anyLong(), Mockito.any(BookingSearchStatus.class),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header(X_SHARER_USER_ID, user.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void getAllByOwnerTest() throws Exception {
        BookingDto bookingDto = BookingMapper.toBookingDto(bookings.get(0));
        User user = users.get(0);
        Mockito
                .when(service.getByOwnerId(Mockito.anyLong(), Mockito.any(BookingSearchStatus.class),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header(X_SHARER_USER_ID, user.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class));
    }
}
