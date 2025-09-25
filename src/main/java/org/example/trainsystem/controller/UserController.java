package org.example.trainsystem.controller;

import org.example.trainsystem.dto.UserDTO;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.UserDAO;
import org.example.trainsystem.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserDAO userDAO;
    private final PassengerService passengerService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserDAO userDAO, PassengerService passengerService, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passengerService = passengerService;
        this.passwordEncoder = passwordEncoder;
    }

    // ================== User registration (after AuthController saves User) ==================
    // This endpoint handles role-specific post-registration logic
    @PostMapping("/postRegister")
    public String handlePostRegister(@ModelAttribute("user") User user) {
        int userId = user.getUserId(); // User is already saved by AuthController

        if ("PASSENGER".equalsIgnoreCase(user.getUserType())) {
            // Create blank passenger record
            Passenger passenger = new Passenger();
            passenger.setUserId(userId);
            passenger.setAddress(""); // blank for now
            passengerService.save(passenger);

            // Redirect passenger to fill address
            return "redirect:/passenger/register/addressUI";
        }

        // Example: handle other roles if needed
        switch (user.getUserType()) {
            case "ItOfficer":
                return "redirect:/it/register?userId=" + userId;
            case "Admin":
                return "redirect:/register/admin?userId=" + userId;
            default:
                return "redirect:/login";
        }
    }

    // ================== Optional: Extra utilities for other roles ==================
    // Keep these if your app uses UserDAO for non-passenger roles
    @GetMapping("/users/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userDAO.findAll());
        return "user/list"; // optional template for admins
    }
}
