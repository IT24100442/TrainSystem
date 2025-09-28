package org.example.trainsystem.controller;

import org.example.trainsystem.auth.PWEncoder;
import org.example.trainsystem.dto.UserDTO;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.PassengerDAO;
import org.example.trainsystem.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserDAO userDAO;
    private final PassengerDAO passengerDAO;

     PWEncoder pwEncoder; // Ensure encoder is injected

    @Autowired
    public UserController(UserDAO userDAO, PassengerDAO passengerDAO) {
        this.userDAO = userDAO;
        this.passengerDAO = passengerDAO;
    }

    // ================= REGISTER PAGE =================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register"; // maps to register.html
    }

    // ================= REGISTER HANDLER =================
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO user, Model model) {
        // ===== Validate passwords =====
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }

        // ===== Map DTO to Entity =====
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());

        // ===== Set userType safely =====
        // Default to "Passenger" if null
        String userType = (user.getUserType() != null && !user.getUserType().isEmpty())
                ? user.getUserType()
                : "Passenger";
        newUser.setUserType(userType);

        // ===== Encode password =====
        newUser.setPassword(pwEncoder.encode(user.getPassword()));

        // ===== Save User =====
        userDAO.saveUser(newUser);
        int userId = newUser.getUserId();
        System.out.println("New user registered: " + newUser);

        // ===== If Passenger, save passenger details =====
        if ("Passenger".equalsIgnoreCase(userType)) {
            Passenger passenger = new Passenger();
            passenger.setUserId(userId);
            passenger.setAddress(user.getAddress());

            try {
                passengerDAO.savePassengerDetails(passenger);
                System.out.println("New passenger registered: " + passenger);
            } catch (Exception e) {
                System.out.println("Error saving passenger: " + e.getMessage());
                model.addAttribute("error", "Error saving passenger details.");
                return "register";
            }
        }

        // ===== Redirect based on userType =====
        switch (userType) {
            case "ItOfficer":
                return "redirect:/it/register?userId=" + userId;
            case "Passenger":
                return "redirect:/passenger/registerAddress?userId=" + userId;
            case "Admin":
                return "redirect:/register/admin?userId=" + userId;
            default:
                return "redirect:/login";
        }
    }
}
