package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BriefBookingDto;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import ru.practicum.shareit.messages.LoggingMessages;
import java.util.List;

/**
 * Controller for bookins
 * @author Evgeniy Lee
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    public static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    // Booking service
    private final BookingService service;

    @PostMapping
    public BookingDto create(@RequestBody BriefBookingDto bookingDto,
                             @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long bookerId) {
        log.debug(LoggingMessages.CREATE.toString(), bookingDto);
        return service.create(bookingDto, bookerId);
    }

    @PatchMapping("{bookingId}")
    public BookingDto approve(@PathVariable Long bookingId, @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId,
                              @RequestParam Boolean approved) {
        log.debug(LoggingMessages.APPROVE_BOOKING.toString().toString(), bookingId);
        return service.approve(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto get(@PathVariable Long bookingId, @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId) {
        log.debug(LoggingMessages.GET.toString(), bookingId);
        return service.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId,
                                         @RequestParam(defaultValue = "ALL") BookingSearchStatus state,
                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug(LoggingMessages.GET_BOOKINGS_BY_USER_ID.toString(), userId);
        return service.getByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    List<BookingDto> getAllByOwner(@RequestHeader(name = X_SHARER_USER_ID_HEADER) long userId,
                                   @RequestParam(defaultValue = "ALL") BookingSearchStatus state,
                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug(LoggingMessages.GET_BOOKINGS_BY_OWNER_ID.toString(), userId);
        return service.getByOwnerId(userId, state, from, size);
    }
}
