package ru.spring.webshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.webshop.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
