package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.PageSettings;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(final ItemRequestDto requestDto, final Long requesterId) {
        User requester = getUserById(requesterId);
        ItemRequest request = RequestMapper.toItemRequest(requestDto);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        return RequestMapper.toItemRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestDto> getAllByRequesterId(Long requesterId, Integer from, Integer size) {
        User requester = getUserById(requesterId);
        Pageable pageable = new PageSettings(from, size, RequestRepository.SORT_CREATE_DATE_ASC);
        List<ItemRequest> requests = requestRepository.findAllByRequesterId(requester.getId(), pageable);
        List<Long> requestIds = requests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        Map<Long, List<Item>> itemsGroupByRequestIds = getItemsByRequestIds(requestIds);
        return RequestMapper.toItemRequestDtoList(requests, itemsGroupByRequestIds);
    }

    @Override
    public ItemRequestDto get(Long requestId, Long requesterId) {
        User requester = getUserById(requesterId);
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
        ItemRequestDto itemRequestDto = RequestMapper.toItemRequestDto(request);
        Map<Long, List<Item>> itemsGroupByRequestIds = getItemsByRequestIds(Arrays.asList(requestId));
        List<Item> items = itemsGroupByRequestIds.get(request.getId());
        itemRequestDto.setItems(ItemMapper.toItemDtoList(items));
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getAllOtherUsers(Long requesterId, Integer from, Integer size) {
        User requester = getUserById(requesterId);
        Pageable pageable = new PageSettings(from, size, RequestRepository.SORT_CREATE_DATE_ASC);
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdNot(requester.getId(), pageable);
        List<Long> requestIds = requests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        Map<Long, List<Item>> itemsGroupByRequestIds = getItemsByRequestIds(requestIds);
        return RequestMapper.toItemRequestDtoList(requests, itemsGroupByRequestIds);
    }

    private Map<Long, List<Item>> getItemsByRequestIds(final List<Long> requestIds) {
        Map<Long, List<Item>> itemsGroupByRequestIds = new HashMap<>();
        List<Item> items = itemRepository.findByRequestIdIn(requestIds);
        for (Item item : items) {
            Long requestId = item.getRequest().getId();
            List<Item> itemsByRequestId = itemsGroupByRequestIds.get(requestId);
            if (itemsByRequestId == null) {
                itemsByRequestId = new ArrayList<>();
            }
            itemsByRequestId.add(item);
            itemsGroupByRequestIds.put(requestId, itemsByRequestId);
        }
        return itemsGroupByRequestIds;
    }

    // Get user from user repository by user ID
    private User getUserById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND));
    }
}
