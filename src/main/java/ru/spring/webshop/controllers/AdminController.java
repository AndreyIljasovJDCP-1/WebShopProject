package ru.spring.webshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.spring.webshop.models.User;
import ru.spring.webshop.services.UserService;

@Controller

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.all());
        return "admin";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String editUser(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/admin/user/edit/{user}")
    public String editUserRole(@PathVariable User user, @RequestParam("role") String role) {
        userService.setRoles(user, role);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/ban/{id}")
    public String banUser(@PathVariable Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }
}
