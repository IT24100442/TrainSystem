package org.example.trainsystem.service;

import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Find user by username - adjust method name based on your UserDAO
            User user = userDAO.findByUsername(username); // Change this to match your actual method

            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            // Create authority based on user role
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserType().toUpperCase());

            // Return Spring Security UserDetails object
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // Must be BCrypt encoded in database
                    .authorities(Collections.singletonList(authority))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();

        } catch (Exception e) {
            throw new UsernameNotFoundException("Error loading user: " + username, e);
        }
    }
}