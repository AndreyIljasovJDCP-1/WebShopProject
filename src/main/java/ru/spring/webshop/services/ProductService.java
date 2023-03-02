package ru.spring.webshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.spring.webshop.models.Image;
import ru.spring.webshop.models.Product;
import ru.spring.webshop.models.User;
import ru.spring.webshop.repositories.ProductRepository;
import ru.spring.webshop.repositories.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Autowired
    UserRepository userRepository;

    public List<Product> getProducts(String title) {
        return title == null
                ? repository.findAll()
                : repository.findByTitleContaining(title).orElseGet(ArrayList::new);
    }

    public void saveProduct(Principal principal, Product product, MultipartFile[] files) throws IOException {
        //добавим пользователя к товару
        product.setUser(getUserByPrincipal(principal));

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
        log.info("Saving new product. Title: {}; PreviewImageId: {}; User(name): {}",
                product.getTitle(), product.getPreviewImageId(),product.getUser().getName());
        repository.save(productDB);

    }

    public User getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElse(null);
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
