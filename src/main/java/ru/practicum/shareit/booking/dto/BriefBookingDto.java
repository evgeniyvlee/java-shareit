package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Class for holding brief info booking
 * @author Evgeniy Lee
 */
@Data
public class BriefBookingDto {
    // Booking ID
    private Long id;

    // Start date
    @NotNull
    @Future
    private LocalDateTime start;

    // End date
    @NotNull
    @Future
    private LocalDateTime end;

    // Item ID
    private Long itemId;

    // Booker ID
    private Long bookerId;

    // Booking status default is waiting
    private BookingStatus status = BookingStatus.WAITING;
}
