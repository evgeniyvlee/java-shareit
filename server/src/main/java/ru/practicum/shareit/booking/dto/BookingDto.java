package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

/**
 * Class for holding booking info
 * @author Evgeniy Lee
 */
@Data
public class BookingDto {
    // Booking ID
    private Long id;

    // Start date
    private LocalDateTime start;

    // End date
    private LocalDateTime end;

    // Item
    private Item item;

    // Booker
    private User booker;

    // Booking status default is waiting
    private BookingStatus status = BookingStatus.WAITING;
}
