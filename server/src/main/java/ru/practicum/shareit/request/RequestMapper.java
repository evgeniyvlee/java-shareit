package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static ItemRequest toItemRequest(final ItemRequestDto requestDto) {
        ItemRequest request = new ItemRequest();
        if (requestDto != null) {
            request.setId(requestDto.getId());
            request.setDescription(requestDto.getDescription());
            request.setCreated(requestDto.getCreated());
        }
        return request;
    }

    public static ItemRequestDto toItemRequestDto(final ItemRequest request) {
        ItemRequestDto requestDto = new ItemRequestDto();
        if (request != null) {
            requestDto.setId(request.getId());
            requestDto.setDescription(request.getDescription());
            requestDto.setCreated(request.getCreated());
        }
        return requestDto;
    }

    public static List<ItemRequestDto> toItemRequestDtoList(
            List<ItemRequest> requests,
            Map<Long, List<Item>> itemsGroupByRequestIds
    ) {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest request : requests) {
            ItemRequestDto itemRequestDto = RequestMapper.toItemRequestDto(request);
            List<Item> items = itemsGroupByRequestIds.get(request.getId());
            itemRequestDto.setItems(ItemMapper.toItemDtoList(items));
            itemRequestDtoList.add(itemRequestDto);
        }
        return itemRequestDtoList;
    }
}
