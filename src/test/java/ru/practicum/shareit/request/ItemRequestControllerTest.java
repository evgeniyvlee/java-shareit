package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @MockBean
    private RequestService service;

    @Autowired
    private MockMvc mockMvc;

    private static Gson gson;

    private final List<ItemRequestDto> requests = new ArrayList<>();

    private final List<UserDto> users = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @BeforeEach
    public void beforeEach() {
        requests.clear();
        ItemRequestDto request1 = new ItemRequestDto();
        request1.setId(1L);
        request1.setDescription("Item request 1 description");
        request1.setCreated(LocalDateTime.now());
        requests.add(request1);

        users.clear();
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@yandex.ru");
        users.add(user1);
    }

    @Test
    public void createItemRequest() throws Exception {
        ItemRequestDto request = requests.get(0);
        UserDto user = users.get(0);
        Mockito.when(service.create(Mockito.any(ItemRequestDto.class), Mockito.anyLong())).thenReturn(request);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/requests")
                                .header(X_SHARER_USER_ID, user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }

    @Test
    public void createItemRequestFailed() throws Exception {
        ItemRequestDto request = requests.get(0);
        request.setDescription("");
        UserDto user = users.get(0);
        Mockito
                .when(service.create(Mockito.any(ItemRequestDto.class), Mockito.anyLong()))
                .thenReturn(request);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/requests")
                        .header(X_SHARER_USER_ID, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getItemRequestTestById() throws Exception {
        ItemRequestDto request = requests.get(0);
        UserDto user = users.get(0);
        Mockito.when(service.get(Mockito.anyLong(), Mockito.anyLong())).thenReturn(request);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/requests/{id}", request.getId())
                        .header(X_SHARER_USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())));
    }

    @Test
    public void getAllByRequesterIdTest() throws Exception {
        ItemRequestDto request = requests.get(0);
        UserDto user = users.get(0);
        Mockito
                .when(service.getAllByRequesterId(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(request));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/requests")
                        .header(X_SHARER_USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void getAllRequestOtherUsersTest() throws Exception {
        ItemRequestDto request = requests.get(0);
        UserDto user = users.get(0);
        Mockito
                .when(service.getAllOtherUsers(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(request));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/requests/all")
                                .header(X_SHARER_USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));

    }
}
