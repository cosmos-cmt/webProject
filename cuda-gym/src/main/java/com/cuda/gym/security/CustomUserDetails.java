package com.cuda.gym.security;

import com.cuda.gym.domain.model.User;
import com.cuda.gym.domain.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String passwordHash;
    private final UserRole role;
    private final boolean banned;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.passwordHash = user.getPasswordHash();
        this.role = user.getRole();
        this.banned = user.isBanned();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isBanned() {
        return banned;
    }

        @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = role != null ? role.name() : "MEMBER";
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + roleName)
        );
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

        @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // no separate account expiration field
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // not tracking credential expiry separately
    }

    @Override
    public boolean isEnabled() {
        return !banned;
    }
}