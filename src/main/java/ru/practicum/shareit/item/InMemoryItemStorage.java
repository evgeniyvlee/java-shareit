package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.data.InMemoryDataStorage;
import ru.practicum.shareit.item.model.Item;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Item storage
 * @author Evgeniy Lee
 */
@Component
@Qualifier("memoryItemStorage")
public class InMemoryItemStorage extends InMemoryDataStorage<Item> implements ItemStorage {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> getByOwner(Long ownerId) {
        List<Item> items = storage.values().stream()
                .filter(i -> ownerId.equals(i.getOwner().getId()))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> search(Long ownerId, String text) {
        List<Item> items = storage.values().stream()
                .filter(i -> (matches(i.getName(), text) || matches(i.getDescription(), text)) && i.getAvailable())
                .collect(Collectors.toList());
        return Collections.unmodifiableList(items);
    }

    // Check is src string contains text
    private boolean matches(String src, String text) {
        return src.toLowerCase(Locale.ROOT).matches("(.*)" + text.toLowerCase(Locale.ROOT) + "(.*)");
    }
}
