package org.example.trainsystem.auth;

import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsersDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UsersDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        String role = user.getUserType();
        System.out.println("Raw userType: " + role);

        if (role == null) {
            throw new RuntimeException("UserType is null for user: " + user.getUsername());
        }


        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserType().toUpperCase());
        System.out.println("Granted authority: " + authority.getAuthority());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}