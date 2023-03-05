package ru.spring.webshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.spring.webshop.models.Role;
import ru.spring.webshop.models.User;
import ru.spring.webshop.repositories.UserRepository;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {

        var email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}; role: {}", email, Role.ROLE_USER);
        userRepository.save(user);
        return true;
    }

    public List<User> all() {
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(!user.isActive());
            log.info("Бан пользователя id: {}; email: {}; active: {};", user.getId(), user.getEmail(), user.isActive());
            userRepository.save(user);
        }
    }

    public void setRoles(User user, String[] roles) {
            user.getRoles().clear();
            for (String role : roles) {
                user.getRoles().add(Role.valueOf(role));
            }
            log.info("Права пользователя id: {}; email: {}; new role: {};", user.getId(), user.getEmail(), user.getRoles());
            userRepository.save(user);
    }
}
