package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;
import java.util.List;

/**
 * Comment repository
 * @author Evgeniy Lee
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemIdOrderById(Long itemId, Sort sort);

    @Query(value = "select c.id, c.text, c.item_id, c.author_id, c.created " +
            "from comments as c, items as i " +
            "where (i.id = c.item_id) and (i.owner_id = :ownerId) " +
            "group by c.item_id"
            , nativeQuery = true)
    List<Comment> findAllByOwnerId(Long ownerId);

    @Query(value = "SELECT comment FROM Comment comment WHERE comment.item.id IN :itemIds")
    List<Comment> findAllByItemIdInOrderById(@Param("itemIds") List<Long> itemIds, Sort sort);
}
