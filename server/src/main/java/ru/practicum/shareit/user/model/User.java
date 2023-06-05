package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.*;

/**
 * User info
 * @author Evgeniy Lee
 */
@Entity
@Table(name = "users")
@Data
public class User {
    // User ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User name
    @Column(length = 255, nullable = false)
    private String name;

    // User email
    @Column(unique = true, length = 255, nullable = false)
    private String email;
}