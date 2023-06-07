package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.messages.LoggingMessages;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingDto bookingDto,
                                         @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long bookerId) {
        log.debug(LoggingMessages.CREATE.toString(), bookingDto);
        return bookingClient.create(bookingDto, bookerId);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable Long bookingId,
                                          @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId,
                                          @RequestParam Boolean approved) {
        log.debug(LoggingMessages.APPROVE_BOOKING.toString().toString(), bookingId);
        return bookingClient.approve(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> get(@PathVariable Long bookingId,
                                      @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId) {
        log.debug(LoggingMessages.GET.toString(), bookingId);
        return bookingClient.get(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId,
                                         @RequestParam(defaultValue = "ALL") BookingSearchStatus state,
                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug(LoggingMessages.GET_BOOKINGS_BY_USER_ID.toString(), userId);
        return bookingClient.getByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(name = X_SHARER_USER_ID_HEADER) long userId,
                                   @RequestParam(defaultValue = "ALL") BookingSearchStatus state,
                                   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                   @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug(LoggingMessages.GET_BOOKINGS_BY_OWNER_ID.toString(), userId);
        return bookingClient.getByOwnerId(userId, state, from, size);
    }
}
