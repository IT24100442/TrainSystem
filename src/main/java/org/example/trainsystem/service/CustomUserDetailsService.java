package org.example.trainsystem.service;

import org.example.trainsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get user from DB
        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Build Spring Security user object
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword()); // already encoded in DB
        builder.roles(user.getUserType()); // e.g., CUSTOMER, OpManager, ADMIN

        return builder.build();
    }

    // Helper method for registering new users with encoded password
    public void registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encrypt password
        userService.createUser(user);
    }
}
