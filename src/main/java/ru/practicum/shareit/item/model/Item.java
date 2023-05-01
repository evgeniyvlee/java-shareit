package ru.practicum.shareit.item.model;

import ru.practicum.shareit.data.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * Item class holds info for items
 * @author Evgeniy Lee
 */
@lombok.Data
public class Item extends Data {
    // Item name
    private String name;
    // Item description
    private String description;
    // Is item available
    private Boolean available;
    // Item owner
    private User owner;
    // Item request for booking
    private ItemRequest request;
}
