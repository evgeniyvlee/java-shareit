package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.LoggingMessages;
import java.util.List;

/**
 * Controller for items
 * @author Evgeniy Lee
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    public static final String X_SHARER_USER_ID_HEADER = "X-Sharer-User-Id";
    // Item service
    private final ItemService service;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long ownerId) {
        log.debug(LoggingMessages.CREATE.toString(), itemDto);
        return service.create(itemDto, ownerId);
    }

    @PatchMapping("{id}")
    public ItemDto update(@PathVariable Long id, @RequestBody ItemDto itemDto,
                          @RequestHeader(name = X_SHARER_USER_ID_HEADER) Long ownerId) {
        log.debug(LoggingMessages.UPDATE.toString(), itemDto);
        return service.update(id, itemDto, ownerId);
    }

    @GetMapping("{id}")
    public ItemDto get(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long userId, @PathVariable Long id) {
        log.debug(LoggingMessages.GET.toString(), id);
        return service.get(id, userId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.debug(LoggingMessages.DELETE.toString(), id);
        service.delete(id);
    }

    @GetMapping
    public List<ItemDto> getByOwner(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long ownerId,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug(LoggingMessages.GET_ITEMS_BY_OWNER_ID.toString());
        return service.getByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(name = X_SHARER_USER_ID_HEADER) Long ownerId, @RequestParam String text,
                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug(LoggingMessages.SEARCH_ITEMS_BY_TEXT.toString());
        return service.search(ownerId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(name = X_SHARER_USER_ID_HEADER) long userId,
                                    @PathVariable long itemId, @RequestBody CommentDto commentDto) {
        log.debug(LoggingMessages.POST_COMMENT.toString(), itemId, commentDto.getText());
        return service.createComment(itemId, userId, commentDto);
    }
}
