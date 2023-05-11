package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;
import java.util.List;

/**
 * Comment repository
 * @author Evgeniy Lee
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemIdOrderByIdAsc(Long itemId);
}
