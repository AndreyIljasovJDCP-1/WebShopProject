package ru.spring.webshop.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.spring.webshop.models.Product;
import ru.spring.webshop.services.ProductService;
import ru.spring.webshop.services.UserService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
public class ProductController {
    @Autowired
    ProductService service;
    @Autowired
    UserService userService;


    @GetMapping("/")
    public String products(
            @RequestParam(name = "title", required = false) String title,
            Model model, Principal principal) {
        model.addAttribute("products", service.getProducts(title));
        model.addAttribute("user", principal);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.getProductById(id));
        return "product-info";
    }

    @PostMapping("/product")
    public String addProduct(Product product,
                             @RequestParam(name = "files") MultipartFile[] files
    ) throws IOException {
        service.saveProduct(product, files);
        return "redirect:/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return "redirect:/products";
    }
}
