package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

public class CommentMapperTest {
    @Test
    public void toCommentTest() {
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L);
        user.setName("User 1");
        user.setEmail("user1@yandex.ru");

        Item item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Item 1 description");
        item.setAvailable(true);

        CommentDto comment = new CommentDto();
        comment.setId(1L);
        comment.setText("Comment 1");
        comment.setAuthorName("User 1");
        comment.setCreated(now);

        Comment actualComment = CommentMapper.toComment(comment, user, item, now);
        Assertions.assertEquals(comment.getId(), actualComment.getId());
        Assertions.assertEquals(user, actualComment.getAuthor());
        Assertions.assertEquals(item, actualComment.getItem());
        Assertions.assertEquals(now, actualComment.getCreated());
        Assertions.assertEquals(comment.getText(), actualComment.getText());
    }
}
