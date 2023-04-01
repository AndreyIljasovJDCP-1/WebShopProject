package ru.spring.webshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.spring.webshop.models.Privilege;
import ru.spring.webshop.models.Role;
import ru.spring.webshop.models.User;
import ru.spring.webshop.repositories.PrivilegeRepository;
import ru.spring.webshop.repositories.RoleRepository;
import ru.spring.webshop.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = true;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege deletePrivilege
                = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(deletePrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        var adminRole = roleRepository.findByName("ROLE_ADMIN");
        var user = new User();
        user.setName("Test");
        user.setPassword(passwordEncoder.encode("1"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setActive(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        var privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        var role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
