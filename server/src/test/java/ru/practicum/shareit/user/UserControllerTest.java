package ru.practicum.shareit.user;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    private static Gson gson;

    private final List<UserDto> users = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
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
        UserDto user3 = new UserDto();
        user3.setId(3L);
        user3.setName("User 3");
        user3.setEmail("user3@yandex.ru");
        users.add(user3);
    }


    @Test
    public void getUserByIdTest() throws Exception {
        UserDto userDto = users.get(0);
        Mockito.when(service.get(Mockito.anyLong())).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }

    @Test
    public void getNotExistUser() throws Exception {
        Mockito
                .doThrow(new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND))
                .when(service).get(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", Mockito.anyLong()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsStringIgnoringCase(ExceptionMessages.DATA_NOT_FOUND)));
    }

    @Test
    public void createUserTest() throws Exception {
        UserDto userDto = users.get(0);
        Mockito.when(service.create(Mockito.any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }

    @Test
    public void createUserWithDuplicateEmailTest() throws Exception {
        UserDto userDto = users.get(0);
        userDto.setEmail(users.get(1).getEmail());

        final String exceptionMessage = "could not execute statement";
        Mockito
                .doThrow(new DataIntegrityViolationException(exceptionMessage))
                .when(service).create(Mockito.any(UserDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                   .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(userDto)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error", containsStringIgnoringCase(exceptionMessage)));
    }

    @Test
    public void updateUserTest() throws Exception {
        UserDto userDto = users.get(0);
        userDto.setName("Updated user 1");
        Mockito.when(service.update(Mockito.anyLong(), Mockito.any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }

    @Test
    public void updateUserFailEmailTest() throws Exception {
        UserDto userDto = users.get(0);
        userDto.setEmail(users.get(1).getEmail());

        final String exceptionMessage = "could not execute statement";
        Mockito
                .doThrow(new DataIntegrityViolationException(exceptionMessage))
                .when(service).update(Mockito.anyLong(), Mockito.any(UserDto.class));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userDto)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error", containsStringIgnoringCase(exceptionMessage)));
    }

    @Test
    public void updateNoExistUserTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(4L);
        userDto.setName("User 4");
        userDto.setEmail("user4@yandex.ru");

        Mockito
                .doThrow(new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND))
                .when(service).update(Mockito.anyLong(), Mockito.any(UserDto.class));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", userDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(userDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsStringIgnoringCase(ExceptionMessages.DATA_NOT_FOUND)));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", users.get(0).getId()))
                .andExpect(status().isOk());
    }
}
