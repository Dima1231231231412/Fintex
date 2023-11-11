package com.example.springapp.service;

import com.example.springapp.database.entity.User;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@EnableWebSecurity
@EnableMethodSecurity
public class CustomUserDetailsService {
    public UserDetailsService userDetailsService(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}
