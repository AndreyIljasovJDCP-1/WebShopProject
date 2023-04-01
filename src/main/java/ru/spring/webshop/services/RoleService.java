package ru.spring.webshop.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spring.webshop.models.Privilege;
import ru.spring.webshop.models.Role;
import ru.spring.webshop.repositories.PrivilegeRepository;
import ru.spring.webshop.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;


    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public void setRolePrivileges(Role role, String[] privileges) {

        role.getPrivileges().clear();
        for (String privilege : privileges) {
            role.getPrivileges().add(createPrivilegeIfNotFound(privilege));
        }
        log.info("Привилегии для роли id: {}; роль: {}; привилегии: {};", role.getId(), role.getName(), role.getListPrivileges());
        roleRepository.save(role);
    }

    @Transactional
    public Role addRoleIfNotFound(
            String name, String[] privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            var list = Arrays.stream(privileges)
                    .map(this::createPrivilegeIfNotFound)
                    .collect(Collectors.toCollection(ArrayList::new));
            role.setPrivileges(list);
            roleRepository.save(role);
            log.info("Новая роль id: {}; роль: {}; привилегии: {};",
                    role.getId(), role.getName(), role.getListPrivileges());
        }
        return role;
    }

}
