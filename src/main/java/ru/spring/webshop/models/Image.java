package ru.spring.webshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "originalFileName")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "contentType")
    private String contentType;
    @Column(name = "isPreview")
    private boolean isPreview;
    @Lob
    private byte[] bytes;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Product product;
}
