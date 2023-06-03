package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private ItemServiceImpl service;

    private List<User> users = new ArrayList<>();
    private Item item = new Item();
    private Comment comment = new Comment();
    private Booking booking = new Booking();

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

        comment.setId(1L);
        comment.setText("Comment 1");
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);

        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Item description 1");
        item.setAvailable(true);
        item.setOwner(user1);

        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStart(LocalDateTime.of(2023, 5, 1, 12, 0));
        booking.setEnd(LocalDateTime.of(2023, 5, 15, 12, 0));
    }

    @Test
    public void createItemTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(users.get(0)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);

        ItemDto itemDto = ItemMapper.toItemDto(item);
        Long ownerId = users.get(0).getId();
        ItemDto savedItemDto = service.create(itemDto, ownerId);

        Assertions.assertNotNull(savedItemDto);
        Assertions.assertEquals(itemDto.getId(), savedItemDto.getId());
        Assertions.assertEquals(itemDto.getName(), savedItemDto.getName());
        Assertions.assertEquals(itemDto.getDescription(), savedItemDto.getDescription());
    }

    @Test
    public void updateItemTest() {
        final User user = users.get(0);

        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName("Item 1");
        updatedItem.setDescription("Item updated description 1");
        updatedItem.setAvailable(true);
        updatedItem.setOwner(user);

        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));

        ItemDto itemDto = ItemMapper.toItemDto(item);
        Long ownerId = user.getId();
        ItemDto savedItemDto = service.update(updatedItem.getId(), ItemMapper.toItemDto(updatedItem), ownerId);

        Assertions.assertNotNull(savedItemDto);
        Assertions.assertEquals(itemDto.getId(), savedItemDto.getId());
        Assertions.assertEquals(itemDto.getName(), savedItemDto.getName());
        Assertions.assertNotEquals(itemDto.getDescription(), savedItemDto.getDescription());
    }

    @Test
    public void getItemTest() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito
                .when(commentRepository
                        .findAllByItemIdInOrderById(Mockito.any(), Mockito.any(Sort.class)))
                .thenReturn(List.of(comment));
        Mockito.when(bookingRepository.findAllByItemIdIn(Mockito.any()))
                .thenReturn(List.of(booking));

        ItemDto itemDto = service.get(item.getId(), users.get(0).getId());

        ItemDto expectedIteDto = ItemMapper.toItemDto(item);
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(expectedIteDto.getId(), itemDto.getId());
        Assertions.assertEquals(booking.getId(), itemDto.getLastBooking().getId());
        Assertions.assertEquals(comment.getId(), itemDto.getComments().get(0).getId());
    }

    @Test
    public void getBuyOwnerTest() {
        Mockito
                .when(itemRepository.findByOwnerId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                        .thenReturn(List.of(item));
        Mockito
                .when(commentRepository
                        .findAllByItemIdInOrderById(Mockito.any(), Mockito.any(Sort.class)))
                .thenReturn(List.of(comment));
        Mockito.when(bookingRepository.findAllByItemIdIn(Mockito.any()))
                .thenReturn(List.of(booking));

        List<ItemDto> itemDtoList = service.getByOwner(users.get(0).getId(), 0, 1);
        ItemDto itemDto = itemDtoList.get(0);

        ItemDto expectedIteDto = ItemMapper.toItemDto(item);
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(expectedIteDto.getId(), itemDto.getId());
        Assertions.assertEquals(booking.getId(), itemDto.getLastBooking().getId());
        Assertions.assertEquals(comment.getId(), itemDto.getComments().get(0).getId());
    }

    @Test
    public void searchTest() {
        Mockito
                .when(itemRepository
                        .findNameOrDescriptionContainingText(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(item));

        List<ItemDto> itemDtoList = service.search(users.get(0).getId(), "Item", 0, 1);
        ItemDto itemDto = itemDtoList.get(0);

        ItemDto expectedIteDto = ItemMapper.toItemDto(item);
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(expectedIteDto.getId(), itemDto.getId());
        Assertions.assertEquals(expectedIteDto.getName(), itemDto.getName());
        Assertions.assertEquals(expectedIteDto.getDescription(), itemDto.getDescription());

    }
}
