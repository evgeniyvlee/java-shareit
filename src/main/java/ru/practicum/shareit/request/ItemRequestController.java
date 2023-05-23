package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LoggingMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    // Request service
    private final RequestService service;

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto requestDto,
                                 @RequestHeader(name = "X-Sharer-User-Id") Long requesterId) {
        log.debug(LoggingMessages.CREATE.toString(), requestDto);
        return service.create(requestDto, requesterId);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto get(@PathVariable Long requestId, @RequestHeader(name = "X-Sharer-User-Id") Long requesterId) {
        log.debug(LoggingMessages.GET.toString(), requestId);
        return service.get(requestId, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequesterId(
            @RequestHeader(name = "X-Sharer-User-Id") Long requesterId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size
    ) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return service.getAllByRequesterId(requesterId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestOtherUsers(
            @RequestHeader(name = "X-Sharer-User-Id") long requesterId,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size
    ) {
        return service.getAllOtherUsers(requesterId, from, size);
    }
}
