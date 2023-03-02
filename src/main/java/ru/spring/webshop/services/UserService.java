package ru.spring.webshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.spring.webshop.models.Role;
import ru.spring.webshop.models.User;
import ru.spring.webshop.repositories.UserRepository;

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
        log.info("Saving new User with email: {}",email);
        userRepository.save(user);
        return true;
    }
    public User getUser(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
}