package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.LoggingMessages;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID_HEADER) Long ownerId,
                                         @Valid @RequestBody ItemDto itemDto) {
        log.debug(LoggingMessages.CREATE.toString(), itemDto);
        return itemClient.create(ownerId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId, @RequestBody ItemDto itemDto,
                                         @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long ownerId) {
        log.debug(LoggingMessages.UPDATE.toString(), itemDto);
        return itemClient.update(itemId, ownerId, itemDto);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(X_SHARER_USER_ID_HEADER) Long ownerId, @PathVariable Long itemId) {
        log.debug(LoggingMessages.GET.toString(), ownerId);
        return itemClient.get(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwner(
            @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long ownerId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug(LoggingMessages.GET_ITEMS_BY_OWNER_ID.toString());
        return itemClient.getByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
                @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId,
                @RequestParam String text,
                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
    ) {
        log.debug(LoggingMessages.SEARCH_ITEMS_BY_TEXT.toString());
        return itemClient.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(name = X_SHARER_USER_ID_HEADER) long userId,
                                    @PathVariable long itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.debug(LoggingMessages.POST_COMMENT.toString(), itemId, commentDto.getText());
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
