package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BriefBookingDto;
import ru.practicum.shareit.booking.model.BookingSearchStatus;
import java.util.List;

/**
 * Booking service
 * @author Evgeniy Lee
 */
public interface BookingService {
    /**
     * Create new booking
     * @param bookingDto booking
     * @param bookerId booker ID
     * @return created booking
     */
    BookingDto create(BriefBookingDto bookingDto, Long bookerId);

    /**
     * Approve booking
     * @param bookingId booking ID
     * @param userId booker ID
     * @param approved is approved
     * @return updated booking
     */
    BookingDto approve(Long bookingId, Long userId, Boolean approved);

    /**
     * Get booking by ID
     * @param bookingId booking ID
     * @param userId user ID
     * @return
     */
    BookingDto get(Long bookingId, Long userId);

    /**
     * Get all bookings by booker
     * @param bookerId booker ID
     * @param status search status
     * @return list of bookings
     */
    List<BookingDto> getByBookerId(Long bookerId, BookingSearchStatus status);

    /**
     * Get all bookings by owner
     * @param ownerId owner ID
     * @param status search status
     * @return list of bookings
     */
    List<BookingDto> getByOwnerId(Long ownerId, BookingSearchStatus status);
}
