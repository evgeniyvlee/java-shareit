package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.messages.LoggingMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto requestDto,
                                         @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId) {
        log.debug(LoggingMessages.CREATE.toString(), requestDto);
        return itemRequestClient.create(requestDto, requesterId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> get(@PathVariable Long requestId,
                                      @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId) {
        log.debug(LoggingMessages.GET.toString(), requestId);
        return itemRequestClient.get(requestId, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequesterId(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return itemRequestClient.getAllByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestOtherUsers(
            @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long requesterId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
    ) {
        return itemRequestClient.getAllOtherUsers(requesterId, from, size);
    }
}
