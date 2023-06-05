package ru.practicum.shareit.item.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Comment  class holds info for comments
 * @author Evgeniy Lee
 */
@Data
public class CommentDto {
    // Comment ID
    private Long id;
    // Comment text
    private String text;
    // Author name
    private String authorName;
    // Created date
    private LocalDateTime created;
}
