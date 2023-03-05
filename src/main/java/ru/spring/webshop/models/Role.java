package ru.spring.webshop.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_LEAD, ROLE_GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
