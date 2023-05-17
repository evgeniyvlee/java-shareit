package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

/**
 * Comment  class holds info for comments
 * @author Evgeniy Lee
 */
@Entity
@Table(name = "comments")
@Data
public class Comment {
    // Comment ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Comment text
    @Column(nullable = false)
    private String text;

    // Commented item
    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    // Comment author
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    // Comment created date
    @Column(nullable = false)
    private LocalDateTime created;
}
