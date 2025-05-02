package com.example.taskmanager.dao;

import com.example.taskmanager.entities.User;
import com.example.taskmanager.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsDao implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsDao(User user) {
        username = user.getUsername();
        password = user.getPassword();
        List<Role> roles = user.getRoles();
        if (roles != null) {
            authorities = roles
                    .stream()
                    .map(role ->
                            new SimpleGrantedAuthority(role.toString()))
                    .toList();
        } else {
            authorities = Collections.emptyList();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
