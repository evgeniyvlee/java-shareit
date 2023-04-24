package ru.practicum.shareit.item;

import ru.practicum.shareit.data.DataStorage;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * Item storage interface
 * @author Evgeniy Lee
 */
public interface ItemStorage extends DataStorage<Item> {
    /**
     * Get item list for user with ID
     * @param ownerId user ID
     * @return item list
     */
    List<Item> getByOwner(Long ownerId);

    /**
     * Search items which contain in name or description text for user with ID
     * @param ownerId user ID
     * @param text searched text
     * @return item list
     */
    List<Item> search(Long ownerId, String text);
}
