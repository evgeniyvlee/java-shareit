package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;
import java.util.List;

/**
 * Item service implementation
 * @author Evgeniy Lee
 */
@Service
public class ItemServiceImpl implements ItemService {
    // Item storage
    private final ItemStorage itemStorage;
    // User storage
    private final UserStorage userStorage;

    public ItemServiceImpl(
            @Qualifier("memoryItemStorage") ItemStorage itemStorage,
            @Qualifier("memoryUserStorage") UserStorage userStorage
    ) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDto create(final ItemDto itemDto, final Long ownerId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(getOwner(ownerId));
        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDto update(final Long id, final ItemDto itemDto, final Long ownerId) {
        Item existItem = itemStorage.get(id);
        Item updateItem = createItemForUpdate(existItem, itemDto, ownerId);
        return ItemMapper.toItemDto(itemStorage.update(updateItem));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemDto get(final Long id) {
        Item item = itemStorage.get(id);
        if (item == null) {
            throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        }
        return ItemMapper.toItemDto(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long id) {
        Item item = itemStorage.get(id);
        if (item == null) {
            throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        }
        itemStorage.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemDto> getByOwner(final Long ownerId) {
        User owner = getOwner(ownerId);
        List<Item> items = itemStorage.getByOwner(owner.getId());
        return ItemMapper.toItemsDto(items);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemDto> search(final Long ownerId, final String text) {
        List<Item> items = null;
        if (text != null && !text.isBlank())
            items = itemStorage.search(ownerId, text);
        return ItemMapper.toItemsDto(items);
    }

    private Item createItemForUpdate(final Item existItem, final ItemDto itemDto, final Long ownerId) {
        if (!existItem.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException(ExceptionMessages.INVALID_OWNER);
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(getOwner(ownerId));
        if (item.getId() == null)
            item.setId(existItem.getId());
        if (item.getName() == null)
            item.setName(existItem.getName());
        if (item.getDescription() == null)
            item.setDescription(existItem.getDescription());
        if (item.getAvailable() == null)
            item.setAvailable(existItem.getAvailable());
        return item;
    }

    private User getOwner(final Long ownerId) {
        User owner = userStorage.get(ownerId);
        if (owner == null) {
            throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        }
        return owner;
    }
}
