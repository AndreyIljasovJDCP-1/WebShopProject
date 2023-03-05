package ru.spring.webshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.spring.webshop.models.Role;
import ru.spring.webshop.models.User;
import ru.spring.webshop.services.UserService;

@Controller
@RequestMapping("/admin/user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping()
    public String admin(Model model) {
        model.addAttribute("users", userService.all());
        return "admin";
    }

    @GetMapping("/edit/{user}")
    public String editUser(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/edit/{user}")
    public String editUserRole(@PathVariable User user, @RequestParam("roles") String[] roles) {
        userService.setRoles(user, roles);
        return "redirect:/admin/user";
    }

    @GetMapping("/ban/{id}")
    public String banUser(@PathVariable Long id) {
        userService.banUser(id);
        return "redirect:/admin/user";
    }
}
