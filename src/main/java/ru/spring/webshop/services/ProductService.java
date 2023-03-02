package ru.spring.webshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.spring.webshop.models.Image;
import ru.spring.webshop.models.Product;
import ru.spring.webshop.repositories.ProductRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    public List<Product> getProducts(String title) {
        return title == null
                ? repository.findAll()
                : repository.findByTitleContaining(title).orElseGet(ArrayList::new);
    }

    public void saveProduct(Product product, MultipartFile[] files) throws IOException {
        for (int i = 0; i < files.length; i++) {
            var image = getImageEntity(files[i]);
            product.addImage(image);
            if (i == 0) {
                image.setPreview(true);
            }
        }
        // получить id картинки
        var productDB = repository.save(product);
        productDB.setPreviewImageId(productDB.getImages().get(0).getId());
        log.info("Saving new product. Title: {}; PreviewImageId: {};", product.getTitle(), product.getPreviewImageId());
        repository.save(productDB);

    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    private Image getImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;

    }
}
