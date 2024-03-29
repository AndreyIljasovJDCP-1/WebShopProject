package ru.spring.webshop.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.spring.webshop.models.Product;
import ru.spring.webshop.repositories.ImagesRepository;
import ru.spring.webshop.services.ProductService;
import ru.spring.webshop.services.UserService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final UserService userService;
    private final ImagesRepository imagesRepository;

    @GetMapping("/")
    public String products(
            @RequestParam(name = "title", required = false) String title,
            Model model, Principal principal) {
        model.addAttribute("products", service.getProducts(title));
        model.addAttribute("images", imagesRepository.findAll());
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "products";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("product", service.getProductById(id));
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "product-info";
    }

    @PostMapping("/product")
    public String addProduct(Principal principal, Product product,
                             @RequestParam(name = "files") MultipartFile[] files
    ) throws IOException {
        service.saveProduct(principal, product, files);
        return "redirect:/";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return "redirect:/";
    }
}
