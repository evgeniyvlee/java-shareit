package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BriefBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
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

        if (bookerId.equals(item.getOwner().getId())) {
            throw new ForbiddenException(ExceptionMessages.ACCESS_DENIED);
        }
        if (!item.getAvailable()) {
            throw new ValidationException(ExceptionMessages.INVALID_DATA);
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isEqual(booking.getStart())) {
            throw new ValidationException(ExceptionMessages.INVALID_DATA);
        }

        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto approve(final Long bookingId, final Long userId, final Boolean approved) {
        Booking booking = getBookingById(bookingId);

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new ForbiddenException(ExceptionMessages.ACCESS_DENIED);
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED) || booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new ValidationException(ExceptionMessages.INVALID_DATA);
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto get(final Long bookingId, final Long userId) {
        Booking booking = getBookingById(bookingId);
        User user = getUserById(userId);
        validateBookingAvailableForUser(booking, user);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getByBookerId(final Long bookerId, final BookingSearchStatus status) {
        User user = getUserById(bookerId);
        List<Booking> bookingList = new ArrayList<>();
        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
                break;
            case CURRENT:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                                bookerId, LocalDateTime.now(), LocalDateTime.now()
                        );
                break;
            case PAST:
                bookingList = bookingRepository
                        .findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
                break;
            case WAITING:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
                break;
            default:
                throw new UnknownStateException(
                        String.format(ExceptionMessages.UNKNOWN_STATE, BookingSearchStatus.UNSUPPORTED_STATUS)
                );
        }
        return BookingMapper.toBookingDtoList(bookingList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getByOwnerId(final Long ownerId, final BookingSearchStatus status) {
        User user = getUserById(ownerId);
        List<Booking> bookingList = new ArrayList<>();

        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId);
                break;
            case CURRENT:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                            ownerId, LocalDateTime.now(), LocalDateTime.now()
                        );
                break;
            case PAST:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case WAITING:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;
            default:
                throw new UnknownStateException(
                        String.format(ExceptionMessages.UNKNOWN_STATE, BookingSearchStatus.UNSUPPORTED_STATUS)
                );
        }
        return BookingMapper.toBookingDtoList(bookingList);
    }

    @Transactional
    private User getUserById(final Long bookerId) {
        return userRepository.findById(bookerId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }

    @Transactional
    private Item getItemById(final Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }

    @Transactional
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
