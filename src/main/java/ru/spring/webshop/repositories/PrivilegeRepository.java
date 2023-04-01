package ru.spring.webshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.webshop.models.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

    Privilege findByName(String name);
}
