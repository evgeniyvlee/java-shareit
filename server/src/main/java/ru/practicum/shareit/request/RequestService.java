package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {
    ItemRequestDto create(ItemRequestDto requestDto, Long requesterId);

    List<ItemRequestDto> getAllByRequesterId(Long requesterId, Integer from, Integer size);

    ItemRequestDto get(Long requestId, Long requesterId);

    List<ItemRequestDto> getAllOtherUsers(Long requesterId, Integer from, Integer size);
}
