package ru.practicum.shareit.item;

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
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.LocalDateTimeAdapter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @MockBean
    private ItemService service;

    @Autowired
    private MockMvc mockMvc;

    private static Gson gson;

    private final List<UserDto> users = new ArrayList<>();

    private final List<ItemDto> items = new ArrayList<>();

    private final List<CommentDto> comments = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @BeforeEach
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
        comment.setText("Comment1");
        comment.setAuthorName(user1.getName());
        comment.setCreated(LocalDateTime.now());
        comments.add(comment);
    }

    @Test
    public void getItemByIdTest() throws Exception {
        ItemDto item = items.get(0);
        UserDto user = users.get(0);
        Mockito.when(service.get(Mockito.anyLong(), Mockito.anyLong())).thenReturn(item);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/items/{id}", item.getId())
                        .header(X_SHARER_USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable()), Boolean.class));
    }

    @Test
    public void createItemTest() throws Exception {
        ItemDto item = items.get(0);
        UserDto user = users.get(0);
        Mockito.when(service.create(Mockito.any(ItemDto.class), Mockito.anyLong())).thenReturn(item);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/items")
                                .header(X_SHARER_USER_ID, user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable()), Boolean.class));
    }

    @Test
    public void updateItemTest() throws Exception {
        ItemDto item = items.get(0);
        UserDto user = users.get(0);

        Mockito.when(service.update(Mockito.anyLong(), Mockito.any(ItemDto.class), Mockito.anyLong())).thenReturn(item);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch("/items/{itemId}", item.getId())
                                .header(X_SHARER_USER_ID, user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable()), Boolean.class));
    }

    @Test
    public void deleteItemTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/{itemId}", items.get(0).getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getByOwnerTest() throws Exception {
        ItemDto item = items.get(0);
        UserDto user = users.get(0);

        Mockito.when(service.getByOwner(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(item));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/items")
                                .header(X_SHARER_USER_ID, user.getId())
                                .param("from", "0")
                                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void searchItemTest() throws Exception {
        ItemDto item = items.get(0);
        UserDto user = users.get(0);

        Mockito.when(service.search(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(item));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/items/search")
                                .header(X_SHARER_USER_ID, user.getId())
                                .param("text", "text")
                                .param("from", "0")
                                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void createCommentTest() throws Exception {
        Mockito
                .when(service.createComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentDto.class)))
                .thenThrow(new BadRequestException(ExceptionMessages.NO_BOOKER_FOR_ITEM));
        ItemDto item = items.get(0);
        UserDto user = users.get(0);
        CommentDto comment = comments.get(0);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/items/{itemId}/comment", item.getId())
                        .header(X_SHARER_USER_ID, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(comment)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error",
                        containsStringIgnoringCase(ExceptionMessages.NO_BOOKER_FOR_ITEM)));
    }
}
