package ru.spring.webshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    private int price;
    private String city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> images = new ArrayList<>();

    private Long previewImageId;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    private LocalDateTime createdDateTime;

    @PrePersist
    private void init() {
        createdDateTime = LocalDateTime.now();
    }

    public Product(String title, String description, int price, String city, String author) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.city = city;
    }

    public void addImage(Image image) {
        image.setProduct(this);
        images.add(image);
    }
}
