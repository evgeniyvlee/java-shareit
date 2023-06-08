package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class for converting comment
 * @author Evgeniy Lee
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    /**
     * Convert comment DTO to comment
     * @param commentDto comment DTO
     * @param user comment author
     * @param item commented item
     * @param created comment created date
     * @return comment instance
     */
    public static Comment toComment(
            final CommentDto commentDto,
            final User user,
            final Item item,
            final LocalDateTime created
    ) {
        Comment comment = new Comment();
        if (commentDto != null) {
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(created);
            comment.setText(commentDto.getText());
        }
        return comment;
    }

    /**
     * Convert comment to comment DTO
     * @param comment comment
     * @return comment DTO instance
     */
    public static CommentDto toCommentDto(final Comment comment) {
        CommentDto commentDto = new CommentDto();
        if (comment != null) {
            commentDto.setId(comment.getId());
            commentDto.setAuthorName(comment.getAuthor().getName());
            commentDto.setText(comment.getText());
            commentDto.setCreated(comment.getCreated());
        }
        return commentDto;
    }

    /**
     * Convert list of comments to list of comment DTO
     * @param comments list of comments
     * @return list of comments DTO
     */
    public static List<CommentDto> toCommentDtoList(final List<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        if (comments != null) {
            comments.stream().forEach(comment -> commentDtoList.add(toCommentDto(comment)));
        }
        return commentDtoList;
    }
}
