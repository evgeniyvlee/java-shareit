package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
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
import java.util.*;
import java.util.stream.Collectors;

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
    // Ascending sort
    //public static final Sort ASC_SORT = Sort.by(Sort.Direction.ASC);

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
        List<Comment> comments = getCommentsByItemIds(Arrays.asList(itemId)).get(itemId);
        itemDto.setComments(CommentMapper.toCommentDtoList(comments));

        if (userId.equals(itemDto.getOwnerId())) {
            Map<Long, List<Booking>> bookingsGroupByItemIds = getBookingsByItemIds(Arrays.asList(itemId));
            Booking lastBooking = getLastBooking(bookingsGroupByItemIds, itemId);
            Booking nextBooking = getNextBooking(bookingsGroupByItemIds, itemId);
            if (lastBooking != null)
                itemDto.setLastBooking(BookingMapper.toBriefBookingDto(lastBooking));
            if (nextBooking != null)
                itemDto.setNextBooking(BookingMapper.toBriefBookingDto(nextBooking));
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
        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());
        Map<Long, List<Booking>> bookingsGroupByItemIds = getBookingsByItemIds(itemIds);
        Map<Long, List<Comment>> commentsGroupByItemIds = getCommentsByItemIds(itemIds);

        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            Long itemId = item.getId();
            Booking lastBooking = getLastBooking(bookingsGroupByItemIds, itemId);
            Booking nextBooking = getNextBooking(bookingsGroupByItemIds, itemId);
            List<Comment> comments = commentsGroupByItemIds.get(itemId);

            if (lastBooking != null)
                itemDto.setLastBooking(BookingMapper.toBriefBookingDto(lastBooking));
            if (nextBooking != null)
                itemDto.setNextBooking(BookingMapper.toBriefBookingDto(nextBooking));
            if (comments != null)
                itemDto.setComments(CommentMapper.toCommentDtoList(comments));

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
                .findAllByBookerIdAndItemIdAndEndBeforeAndStatus(userId, itemId, created, BookingStatus.APPROVED,
                        BookingRepository.SORT_START_DATE_DESC);
        if (bookerItems.isEmpty()) {
            throw new BadRequestException(ExceptionMessages.NO_BOOKER_FOR_ITEM);
        }
        Comment comment = CommentMapper.toComment(commentDto, user, item, created);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    private Map<Long, List<Booking>> getBookingsByItemIds(final List<Long> itemIds) {
        Map<Long, List<Booking>> bookingsGroupByItemIds = new HashMap<>();
        List<Booking> bookings = bookingRepository.findAllByItemIdIn(itemIds);
        for (Booking booking : bookings) {
            Long itemId = booking.getItem().getId();
            List<Booking> bookingsByItemId = bookingsGroupByItemIds.get(itemId);
            if (bookingsByItemId == null) {
                bookingsByItemId = new ArrayList<>();
            }
            bookingsByItemId.add(booking);
            bookingsGroupByItemIds.put(itemId, bookingsByItemId);
        }
        return bookingsGroupByItemIds;
    }

    private Booking getLastBooking(final Map<Long, List<Booking>> bookingsGroupByItemIds, final Long itemId) {
        List<Booking> bookings = bookingsGroupByItemIds.get(itemId);
        Booking lastBooking = null;
        if (bookings != null) {
            List<Booking> lastBookings = bookings
                    .stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart).reversed())
                    .collect(Collectors.toList());
            if (!lastBookings.isEmpty())
                lastBooking = lastBookings.get(0);
        }
        return lastBooking;
    }

    private Booking getNextBooking(final Map<Long, List<Booking>> bookingsGroupByItemIds, final Long itemId) {
        List<Booking> bookings = bookingsGroupByItemIds.get(itemId);
        Booking nextBooking = null;
        if (bookings != null) {
            List<Booking> nextBookings = bookings
                    .stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .collect(Collectors.toList());
            if (!nextBookings.isEmpty())
                nextBooking = nextBookings.get(0);
        }
        return nextBooking;
    }

    @Transactional(readOnly = true)
    private Map<Long, List<Comment>> getCommentsByItemIds(final List<Long> itemIds) {
        Map<Long, List<Comment>> commentsGroupByItemIds = new HashMap<>();
        List<Comment> comments = commentRepository.findAllByItemIdInOrderById(itemIds, Sort.by(Sort.Direction.ASC, "id"));
        for (Comment comment : comments) {
            Long itemId = comment.getItem().getId();
            List<Comment> commentsByItemId = commentsGroupByItemIds.get(itemId);
            if (commentsByItemId == null) {
                commentsByItemId = new ArrayList<>();
            }
            commentsByItemId.add(comment);
            commentsGroupByItemIds.put(itemId, commentsByItemId);
        }
        return commentsGroupByItemIds;
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
