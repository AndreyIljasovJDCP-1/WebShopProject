package ru.spring.webshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.webshop.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
