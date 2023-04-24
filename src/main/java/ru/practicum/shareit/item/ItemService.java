package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

/**
 * Item service interface for CRUD operations
 * @author Evgeniy Lee
 */
public interface ItemService {
    /**
     * Create item by user with ID
     * @param itemDto item
     * @param ownerId item owner ID
     * @return created item
     */
    ItemDto create(ItemDto itemDto, Long ownerId);

    /**
     * Update item
     * @param id item ID
     * @param itemDto updated iem
     * @param ownerId item owner ID
     * @return updated item
     */
    ItemDto update(Long id, ItemDto itemDto, Long ownerId);

    /**
     * Get item by ID
     * @param id item ID
     * @return item
     */
    ItemDto get(Long id);

    /**
     * Delete item by ID
     * @param id item Id
     */
    void delete(Long id);

    /**
     * Get item list for user with ID
     * @param ownerId user ID
     * @return item list
     */
    List<ItemDto> getByOwner(Long ownerId);

    /**
     * Search items which contain in name or description text for user with ID
     * @param ownerId user ID
     * @param text searched text
     * @return item list
     */
    List<ItemDto> search(Long ownerId, String text);
}
