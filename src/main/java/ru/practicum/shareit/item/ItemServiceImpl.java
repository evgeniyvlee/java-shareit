package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Item service implementation
 * @author Evgeniy Lee
 */
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    // Item DB repository
    private final ItemRepository itemRepository;
    // User DB repository
    private final UserRepository userRepository;
    // Comment DB repository
    private final CommentRepository commentRepository;
    // Booking DB repository
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public ItemDto create(final ItemDto itemDto, final Long ownerId) {
        User owner = getUserById(ownerId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(final Long itemId, final ItemDto itemDto, final Long ownerId) {
        Item item = getItemById(itemId);

        if (!ownerId.equals(item.getOwner().getId())) {
            throw new ForbiddenException(ExceptionMessages.ACCESS_DENIED);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto get(final Long itemId, final Long userId) {
        Item item = getItemById(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        addCommentsByItem(itemDto);
        if (userId.equals(itemDto.getOwnerId())) {
            addBookingsByItem(itemDto);
        }
        return itemDto;
    }

    @Transactional
    @Override
    public void delete(final Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getByOwner(final Long ownerId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            addCommentsByItem(itemDto);
            addBookingsByItem(itemDto);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> search(final Long ownerId, final String text) {
        if ((text == null) || (text.isBlank())) {
            return new ArrayList<>();
        } else {
            return ItemMapper.toItemDtoList(itemRepository.findNameOrDescriptionContainingText(text));
        }
    }

    @Transactional
    @Override
    public CommentDto createComment(final Long itemId, final Long userId, final CommentDto commentDto) {
        User user = getUserById(userId);
        Item item = getItemById(itemId);
        LocalDateTime created = LocalDateTime.now();
        List<Booking> bookerItems = bookingRepository
                .findAllByBookerIdAndItemIdAndEndBeforeAndStatus(userId, itemId, created, BookingStatus.APPROVED);
        if (bookerItems.isEmpty()) {
            throw new BadRequestException("ssss");
        }
        Comment comment = CommentMapper.toComment(commentDto, user, item, created);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    // Add bookings to item by item ID
    @Transactional(readOnly = true)
    private void addBookingsByItem(final ItemDto itemDto) {
        Long itemId = itemDto.getId();
        LocalDateTime now = LocalDateTime.now();

        List<Booking> lastBookings = bookingRepository
                .findAllByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemId, now, BookingStatus.APPROVED);
        List<Booking> nextBookings = bookingRepository
                .findAllByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, now, BookingStatus.APPROVED);

        itemDto.setLastBooking(lastBookings.isEmpty() ? null : BookingMapper.toBriefBookingDto(lastBookings.get(0)));
        itemDto.setNextBooking(nextBookings.isEmpty() ? null : BookingMapper.toBriefBookingDto(nextBookings.get(0)));
    }

    // Add comments to item by item ID
    @Transactional(readOnly = true)
    private void addCommentsByItem(final ItemDto itemDto) {
        List<Comment> comments = commentRepository.findAllByItemIdOrderByIdAsc(itemDto.getId());
        itemDto.setComments(CommentMapper.toCommentDtoList(comments));
    }

    // Get user from user repository by user ID
    @Transactional(readOnly = true)
    private User getUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }

    // Get item from item repository by item ID
    @Transactional(readOnly = true)
    private Item getItemById(final Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }
}
