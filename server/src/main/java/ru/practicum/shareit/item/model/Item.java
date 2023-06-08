package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 * Item class holds info for items
 * @author Evgeniy Lee
 */
@Entity
@Table(name = "items")
@Data
public class Item {
    // Item ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Item name
    @Column(length = 255, nullable = false)
    private String name;

    // Item description
    @Column(length = 1000, nullable = false)
    private String description;

    // Is item available
    @Column(name = "is_available")
    private Boolean available;

    // Item owner
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    // Item request for booking
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
