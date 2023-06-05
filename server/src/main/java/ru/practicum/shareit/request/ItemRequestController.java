package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.messages.LoggingMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    public static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    // Request service
    private final RequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto requestDto,
                                 @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId) {
        log.debug(LoggingMessages.CREATE.toString(), requestDto);
        return service.create(requestDto, requesterId);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto get(@PathVariable Long requestId,
                              @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId) {
        log.debug(LoggingMessages.GET.toString(), requestId);
        return service.get(requestId, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequesterId(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId,
                                                    @RequestParam(name = "from") Integer from,
                                                    @RequestParam(name = "size") Integer size) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return service.getAllByRequesterId(requesterId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestOtherUsers(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId,
                                                        @RequestParam(name = "from") Integer from,
                                                        @RequestParam(name = "size") Integer size) {
        return service.getAllOtherUsers(requesterId, from, size);
    }
}
