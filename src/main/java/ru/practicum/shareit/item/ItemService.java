package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
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
     * @param itemId item ID
     * @param itemDto updated iem
     * @param ownerId item owner ID
     * @return updated item
     */
    ItemDto update(Long itemId, ItemDto itemDto, Long ownerId);

    /**
     * Get item by ID
     * @param itemId item ID
     * @return item
     */
    ItemDto get(Long itemId, Long userId);

    /**
     * Delete item by ID
     * @param itemId item Id
     */
    void delete(Long itemId);

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

    /**
     * Create comment by author for item
     * @param itemId item ID
     * @param userId author ID
     * @param commentDto comment
     * @return created comment
     */
    CommentDto createComment(Long itemId, Long userId, CommentDto commentDto);
}
