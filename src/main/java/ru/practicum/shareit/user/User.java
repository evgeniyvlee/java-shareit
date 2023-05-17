package ru.practicum.shareit.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

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