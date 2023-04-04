package ru.spring.webshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.spring.webshop.models.Image;
import ru.spring.webshop.models.User;
import ru.spring.webshop.repositories.ImagesRepository;
import ru.spring.webshop.repositories.RoleRepository;
import ru.spring.webshop.repositories.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ImagesRepository imagesRepository;

    public boolean createUser(User user, String role) {

        var email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(
                roleRepository.findByName(role)));
        userRepository.save(user);
        log.info("Saving new User with email: {}; role: {}", email, role);

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
            user.getRoles().add(roleRepository.findByName(role));
        }
        log.info("Права пользователя id: {}; email: {}; new roles: {};", user.getId(), user.getEmail(), user.getRoleList());
        userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        return principal == null
                ? null
                : userRepository.findByEmail(principal.getName())
                .orElseThrow();
    }

    public void setAvatar(User user, MultipartFile file) throws IOException {
        if (Objects.nonNull(user.getAvatar())) {
            var idAvatar = user.getAvatar().getId();
            user.setAvatar(null);
            imagesRepository.deleteById(idAvatar);
        }
        user.setAvatar(getImageEntity(file));
        userRepository.save(user);

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
