package org.example.trainsystem.controller;

import org.example.trainsystem.entity.User;
import org.example.trainsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Login page
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // maps to login.html
    }

    // ✅ Registration page (GET)
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // maps to register.html
    }

    // ✅ Handle registration form (POST)
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default userType if not set
        if (user.getUserType() == null || user.getUserType().isEmpty()) {
            user.setUserType("PASSENGER");
        }

        try {
            userService.createUser(user);
            model.addAttribute("successMessage", "Registration successful! You can now log in.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}
