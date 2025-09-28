package org.example.trainsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    // ================== SHOW LOGIN PAGE ==================
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }

        if (logout != null) {
            model.addAttribute("logout", "You have been logged out successfully.");
        }

        return "login"; // maps to login.html in templates
    }

    // ================== HOME/ROOT REDIRECT ==================
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {
            // User is logged in, redirect to appropriate dashboard based on role
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            switch (role) {
                case "ROLE_PASSENGER":
                    return "redirect:/passenger/dashboard";
                case "ROLE_ADMIN":
                    return "redirect:/admin/dashboard";
                case "ROLE_OPMANAGER":
                    return "redirect:/opmanager/dashboard";
                case "ROLE_DRIVER":
                    return "redirect:/driver/dashboard";
                case "ROLE_ITOFFICER":
                    return "redirect:/it/dashboard";
                default:
                    return "redirect:/login";
            }
        }
        // User is not authenticated, show home page or redirect to login
        return "home"; // or return "home"; if you want to show a home page first
    }
}