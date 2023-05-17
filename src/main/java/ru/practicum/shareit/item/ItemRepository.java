package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

/**
 * Item repository
 * @author Evgeniy Lee
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(long ownerId);

    @Query(value = "select * " +
            "from items " +
            "where is_available and (" +
            "   (lower(name) like lower(concat('%', :text, '%'))) " +
            "       or (lower(description) like lower(concat('%', :text, '%')))" +
            ")", nativeQuery = true)
    List<Item> findNameOrDescriptionContainingText(@Param("text") String text);
}
