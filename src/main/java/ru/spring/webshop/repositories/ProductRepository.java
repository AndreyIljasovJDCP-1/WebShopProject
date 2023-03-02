package ru.spring.webshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.webshop.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<List<Product>> findByTitleContaining(String title);
}
