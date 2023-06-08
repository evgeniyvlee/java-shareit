package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BriefBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Util class for converting booking
 * @author Evgeniy Lee
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    /**
     * Convert booking DTO to booking
     * @param bookingDto booking DTO
     * @param booker booker
     * @param item item
     * @return booking
     */
    public static Booking toBooking(final BriefBookingDto bookingDto, final User booker, final Item item) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    /**
     * Convert booking to booking DTO
     * @param booking booking instance
     * @return booking DTO instance
     */
    public static BookingDto toBookingDto(final Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setItem(booking.getItem());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    /**
     * Convert booking to booking DTO
     * @param booking booking instance
     * @return booking DTO instance
     */
    public static BriefBookingDto toBriefBookingDto(final Booking booking) {
        BriefBookingDto briefBookingDto = new BriefBookingDto();
        briefBookingDto.setId(booking.getId());
        briefBookingDto.setBookerId(booking.getBooker().getId());
        briefBookingDto.setItemId(booking.getItem().getId());
        briefBookingDto.setStart(booking.getStart());
        briefBookingDto.setEnd(booking.getEnd());
        briefBookingDto.setStatus(booking.getStatus());
        return briefBookingDto;
    }

    /**
     * Convert list of bookings to list of bookings DTO
     * @param bookings list of bookings
     * @return list of bookings DTO
     */
    public static List<BookingDto> toBookingDtoList(final List<Booking> bookings) {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookings.stream()
                .forEach(b -> bookingDtoList.add(toBookingDto(b)));
        return Collections.unmodifiableList(bookingDtoList);
    }
}
