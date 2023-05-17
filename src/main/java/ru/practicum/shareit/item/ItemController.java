package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.LoggingMessages;
import javax.validation.Valid;
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

    // Item service
    private final ItemService service;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto, @RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        log.debug(LoggingMessages.CREATE.toString(), itemDto);
        return service.create(itemDto, ownerId);
    }

    @PatchMapping("{id}")
    public ItemDto update(
            @PathVariable Long id,
            @RequestBody ItemDto itemDto,
            @RequestHeader(name = "X-Sharer-User-Id") Long ownerId
    ) {
        log.debug(LoggingMessages.UPDATE.toString(), itemDto);
        return service.update(id, itemDto, ownerId);
    }

    @GetMapping("{id}")
    public ItemDto get(@RequestHeader(name = "X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        log.debug(LoggingMessages.GET.toString(), id);
        return service.get(id, userId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.debug(LoggingMessages.DELETE.toString(), id);
        service.delete(id);
    }

    @GetMapping
    public List<ItemDto> getByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        log.debug(LoggingMessages.GET_ITEMS_BY_OWNER_ID.toString());
        return service.getByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId, @RequestParam String text) {
        log.debug(LoggingMessages.SEARCH_ITEMS_BY_TEXT.toString());
        return service.search(ownerId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(
            @RequestHeader(name = "X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @Valid @RequestBody CommentDto commentDto
    ) {
        log.debug(LoggingMessages.POST_COMMENT.toString(), itemId, commentDto.getText());
        return service.createComment(itemId, userId, commentDto);
    }
}
