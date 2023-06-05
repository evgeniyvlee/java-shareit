package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BriefBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.PageSettings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Booking service
 * @author Evgeniy Lee
 */
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    // Booking repository
    private final BookingRepository bookingRepository;
    // Item repository
    private final ItemRepository itemRepository;
    // User repository
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto create(final BriefBookingDto bookingDto, final Long bookerId) {
        User booker = getUserById(bookerId);
        Item item = getItemById(bookingDto.getItemId());

        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);

        if (validateUserIsItemOwner(bookerId, item)) {
            throw new ForbiddenException(ExceptionMessages.ACCESS_DENIED);
        }

        if (!item.getAvailable()) {
            throw new ValidationException(ExceptionMessages.INVALID_DATA);
        }

        boolean endBeforeStart = booking.getEnd().isBefore(booking.getStart());
        boolean endSameStart = booking.getEnd().isEqual(booking.getStart());
        if (endBeforeStart || endSameStart) {
            throw new ValidationException(ExceptionMessages.INVALID_DATA);
        }

        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto approve(final Long bookingId, final Long userId, final Boolean approved) {
        Booking booking = getBookingById(bookingId);

        if (!validateUserIsItemOwner(userId, booking.getItem())) {
            throw new ForbiddenException(ExceptionMessages.ACCESS_DENIED);
        }

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException(ExceptionMessages.INVALID_DATA);
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(booking);
    }

    private boolean validateUserIsItemOwner(Long userId, Item item) {
        return userId.equals(item.getOwner().getId());
    }

    @Override
    public BookingDto get(final Long bookingId, final Long userId) {
        Booking booking = getBookingById(bookingId);
        User user = getUserById(userId);
        validateBookingAvailableForUser(booking, user);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getByBookerId(
            final Long bookerId, final BookingSearchStatus status, final Integer from, final Integer size
    ) {
        User user = getUserById(bookerId);
        Pageable pageable = new PageSettings(from, size, BookingRepository.SORT_START_DATE_DESC);
        List<Booking> bookingList = new ArrayList<>();
        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerId(bookerId, pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookingList = bookingRepository
                        .findAllByBookerIdAndEndBefore(bookerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStartAfter(bookerId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStatus(bookerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, pageable);
                break;
            default:
                throw new UnknownStateException(
                        String.format(ExceptionMessages.UNKNOWN_STATE, BookingSearchStatus.UNSUPPORTED_STATUS)
                );
        }
        return BookingMapper.toBookingDtoList(bookingList);
    }

    @Override
    public List<BookingDto> getByOwnerId(
            final Long ownerId, final BookingSearchStatus status, final Integer from, final Integer size
    ) {
        User user = getUserById(ownerId);
        Pageable pageable = new PageSettings(from, size, BookingRepository.SORT_START_DATE_DESC);
        List<Booking> bookingList = new ArrayList<>();

        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerId(ownerId, pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStartAfter(ownerId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED, pageable);
                break;
            default:
                throw new UnknownStateException(
                        String.format(ExceptionMessages.UNKNOWN_STATE, BookingSearchStatus.UNSUPPORTED_STATUS)
                );
        }
        return BookingMapper.toBookingDtoList(bookingList);
    }

    private User getUserById(final Long bookerId) {
        return userRepository.findById(bookerId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }

    private Item getItemById(final Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }

    private Booking getBookingById(final Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }

    private boolean validateBookingAvailableForUser(final Booking booking, final User user) {
        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();
        Long userId = user.getId();
        if (userId.equals(bookerId) || userId.equals(ownerId)) {
            return true;
        } else {
            throw new ForbiddenException(ExceptionMessages.ACCESS_DENIED);
        }
    }
}
