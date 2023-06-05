package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Item class holds info for items
 * @author Evgeniy Lee
 */
@Data
public class ItemDto {
    // Item ID
    private Long id;
    // Item name
    @NotBlank
    private String name;
    // Item description
    @NotBlank
    private String description;
    // Is item available
    @NotNull
    private Boolean available;
    // Item owner ID
    @JsonIgnore
    private Long ownerId;
    // List of comments
    private List<CommentDto> comments;
    // Last booking
    private BookingDto lastBooking;
    // Next booking
    private BookingDto nextBooking;
    // Request ID
    private Long requestId;
}
