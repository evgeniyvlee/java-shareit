package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.practicum.shareit.data.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Item class holds info for items
 * @author Evgeniy Lee
 */
@lombok.Data
public class ItemDto extends Data {
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
}
