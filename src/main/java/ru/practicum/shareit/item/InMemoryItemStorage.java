package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.data.InMemoryDataStorage;
import ru.practicum.shareit.item.model.Item;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.Locale;

/**
 * Item storage
 * @author Evgeniy Lee
 */
@Component
@Qualifier("memoryItemStorage")
public class InMemoryItemStorage extends InMemoryDataStorage<Item> implements ItemStorage {
    // Locale
    public static final Locale LOCALE = Locale.ROOT;
    // Map holds user ID and its items IDs
    private final Map<Long, Set<Long>> userItemsMap = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Item create(Item item) {
        Item newItem = super.create(item);
        addToUserItemsMap(newItem.getOwner().getId(), newItem.getId());
        return newItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item update(Item item) {
        addToUserItemsMap(item.getOwner().getId(), item.getId());
        return super.update(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        Long ownerId = super.get(id).getOwner().getId();
        userItemsMap.get(ownerId).remove(id);
        super.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> getByOwner(Long ownerId) {
        Set<Long> ownerItems = userItemsMap.get(ownerId);
        List<Item> items = ownerItems.stream().map(storage::get).collect(Collectors.toList());
        return Collections.unmodifiableList(items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> search(Long ownerId, String text) {
        List<Item> items = storage.values().stream()
                .filter(i -> i.getAvailable() && (matches(i.getName(), text) || matches(i.getDescription(), text)))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(items);
    }

    // Check is src string contains text
    private boolean matches(String src, String text) {
        return src.toLowerCase(LOCALE).matches("(.*)" + text.toLowerCase(LOCALE) + "(.*)");
    }

    // Add owner ID and item ID to userItemsMap
    private void addToUserItemsMap(final Long userId, final Long itemId) {
        Set<Long> userItems = userItemsMap.getOrDefault(userId, new HashSet<>());
        userItems.add(itemId);
        userItemsMap.put(userId, userItems);
    }
}
