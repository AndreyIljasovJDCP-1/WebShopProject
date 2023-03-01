package ru.spring.webshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spring.webshop.models.Image;

public interface ImagesRepository extends JpaRepository<Image,Long> {
}
