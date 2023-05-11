package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User repository
 * @author Evgeniy Lee
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
