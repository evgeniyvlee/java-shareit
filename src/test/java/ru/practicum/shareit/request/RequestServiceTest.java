package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    private final List<ItemRequestDto> requests = new ArrayList<>();
    private final List<UserDto> users = new ArrayList<>();

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
    public void createRequestTest() {
        UserDto user = users.get(0);
        ItemRequestDto expectedRequest = requests.get(0);
        Mockito
                .when(requestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(RequestMapper.toItemRequest(expectedRequest));

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(user)));

        ItemRequestDto actualRequest = requestService.create(expectedRequest, user.getId());
        Assertions.assertEquals(expectedRequest.getId(), actualRequest.getId());
        Assertions.assertEquals(expectedRequest.getDescription(), actualRequest.getDescription());
    }

    @Test
    public void getRequestTest() {
        UserDto user = users.get(0);
        ItemRequestDto expectedRequest = requests.get(0);
        Mockito
                .when(requestRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(RequestMapper.toItemRequest(expectedRequest)));
        Mockito
                .when(itemRepository.findByRequestIdIn(Mockito.anyList()))
                        .thenReturn(List.of());
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                    .thenReturn(Optional.of(UserMapper.toUser(user)));

        ItemRequestDto actualRequest = requestService.get(expectedRequest.getId(), user.getId());
        Assertions.assertEquals(expectedRequest.getId(), actualRequest.getId());
        Assertions.assertEquals(expectedRequest.getDescription(), actualRequest.getDescription());
    }

    @Test
    public void getAllByRequesterIdTest() {
        UserDto user = users.get(0);
        ItemRequestDto expectedRequest = requests.get(0);
        Mockito
                .when(requestRepository.findAllByRequesterId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                    .thenReturn(List.of(RequestMapper.toItemRequest(expectedRequest)));
        Mockito
                .when(itemRepository.findByRequestIdIn(Mockito.anyList()))
                .thenReturn(List.of());
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(user)));

        List<ItemRequestDto> actualRequests = requestService.getAllByRequesterId(user.getId(), 0, 1);
        Assertions.assertEquals(1, actualRequests.size());
        ItemRequestDto actualRequest = actualRequests.get(0);
        Assertions.assertEquals(expectedRequest.getId(), actualRequest.getId());
        Assertions.assertEquals(expectedRequest.getDescription(), actualRequest.getDescription());
    }

    @Test
    public void getAllOtherUsersTest() {
        UserDto user = users.get(0);
        ItemRequestDto expectedRequest = requests.get(0);
        Mockito
                .when(requestRepository.findAllByRequesterIdNot(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(RequestMapper.toItemRequest(expectedRequest)));
        Mockito
                .when(itemRepository.findByRequestIdIn(Mockito.anyList()))
                .thenReturn(List.of());
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(user)));

        List<ItemRequestDto> actualRequests = requestService.getAllOtherUsers(user.getId(), 0, 1);
        Assertions.assertEquals(1, actualRequests.size());
        ItemRequestDto actualRequest = actualRequests.get(0);
        Assertions.assertEquals(expectedRequest.getId(), actualRequest.getId());
        Assertions.assertEquals(expectedRequest.getDescription(), actualRequest.getDescription());
    }
}
