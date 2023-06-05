package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BriefBookingDto;
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
    private String name;
    // Item description
    private String description;
    // Is item available
    private Boolean available;
    // Item owner ID
    @JsonIgnore
    private Long ownerId;
    // List of comments
    private List<CommentDto> comments;
    // Last booking
    private BriefBookingDto lastBooking;
    // Next booking
    private BriefBookingDto nextBooking;
    // Request ID
    private Long requestId;
}
