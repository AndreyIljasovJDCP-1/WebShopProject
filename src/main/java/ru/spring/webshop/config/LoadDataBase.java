package ru.spring.webshop.config;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.spring.webshop.models.Product;
import ru.spring.webshop.repositories.ProductRepository;

/**
 * Класс для:<br>
 * а) предварительной загрузки данных, при хранении данных in MEMORY:H2 Hibernate<br>
 * б) 1-й загрузки в mySQL (создание таблицы),
 * после создания таблицы переключить userTable.data.preload=false  в application.properties
 */
@Log
@Configuration
public class LoadDataBase {

    @Bean
    @ConditionalOnProperty(name = "productTable.data.preload", havingValue = "true")
    CommandLineRunner initDatabase(ProductRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(
                    new Product("PS5", "playstation5", 67000, "Moscow", "andy")));
            log.info("Preloading " + repository.save(
                    new Product( "iphone 10", "iphone 10", 57000, "Petersburg", "mandy")));
        };
    }
}

