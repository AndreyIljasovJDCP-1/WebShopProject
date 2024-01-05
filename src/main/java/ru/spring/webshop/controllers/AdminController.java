package ru.spring.webshop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.spring.webshop.models.Role;
import ru.spring.webshop.models.User;
import ru.spring.webshop.repositories.PrivilegeRepository;
import ru.spring.webshop.repositories.RoleRepository;
import ru.spring.webshop.services.RoleService;
import ru.spring.webshop.services.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RoleService roleService;

    @GetMapping()
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.all());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "admin";
    }

    @GetMapping("/edit/{user}")
    public String editUser(@PathVariable User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
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

    @GetMapping("/role/edit/{role}")
    public String editRole(@PathVariable Role role, Model model, Principal principal) {
        model.addAttribute("role", role);
        model.addAttribute("listPrivileges", privilegeRepository.findAll());
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "role-edit";
    }

    @PostMapping("/role/edit/{role}")
    public String editRolePrivilege(@PathVariable Role role, @RequestParam("privileges") String[] privileges) {
        roleService.setRolePrivileges(role, privileges);
        return "redirect:/admin/user";
    }

    @GetMapping("/role/create")
    public String addRole(Model model, Principal principal) {
        model.addAttribute("authUser", userService.getUserByPrincipal(principal));
        return "role-create";
    }

    @PostMapping("/role/create")
    public String createRole(@RequestParam("role") String role, @RequestParam("privileges") String[] privileges) {
        roleService.addRoleIfNotFound(role, privileges);
        return "redirect:/admin/user";
    }
}
