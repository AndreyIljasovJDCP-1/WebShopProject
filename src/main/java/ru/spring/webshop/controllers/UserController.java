package ru.spring.webshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.spring.webshop.models.User;
import ru.spring.webshop.services.UserService;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorCreateUser", user.getEmail());
            return "registration";
        }
        return "redirect:/";
    }

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        var authUser = principal == null ? null : userService.getUserByPrincipal(principal);
        model.addAttribute("authUser", authUser);
        return "user-info";
    }
}
