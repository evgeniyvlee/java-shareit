package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class for converting item
 * @author Evgeniy Lee
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    /**
     * Convert item to item DTO
     * @param item item
     * @return item DTO
     */
    public static ItemDto toItemDto(final Item item) {
        ItemDto itemDto = new ItemDto();
        if (item != null) {
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setDescription(item.getDescription());
            itemDto.setAvailable(item.getAvailable());
            itemDto.setOwnerId(item.getOwner().getId());
        }
        return itemDto;
    }

    /**
     * Convert item DTO to item
     * @param itemDto item DTO
     * @return item
     */
    public static Item toItem(final ItemDto itemDto) {
        Item item = new Item();
        if (itemDto != null) {
            item.setId(itemDto.getId());
            item.setName(itemDto.getName());
            item.setDescription(itemDto.getDescription());
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    /**
     * Convert list of items to list of items DTO
     * @param items list of items
     * @return list of items DTO
     */
    public static List<ItemDto> toItemsDto(final List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList<>();
        if (items != null)
            items.stream().forEach(i -> itemsDto.add(ItemMapper.toItemDto(i)));
        return itemsDto;
    }
}
