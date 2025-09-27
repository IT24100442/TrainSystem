package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.dto.UserDTO;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.service.UserService;
import org.example.trainsystem.service.PassengerService;
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

    @Autowired
    private PassengerService passengerService;

    // ================= LOGIN =================
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new UserDTO()); // form backing bean
        return "login"; // maps to login.html
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserDTO loginDTO,
                            HttpSession session,
                            Model model) {
        User user = userService.findByUsername(loginDTO.getUsername());

        if (user != null || passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            model.addAttribute("error", "Invalid username or password");
            session.setAttribute("loggedInUser", user);

            String userType = user.getUserType().trim().toUpperCase();

            if ("PASSENGER".equals(userType)) {
                // fetch passenger object safely
                Passenger passenger = passengerService.getPassengerWithUser(user.getUsername());
                session.setAttribute("passenger", passenger);

                // redirect to controller mapping, not template
                return "redirect:/passenger/dashboard";
            } else if ("ITOFFICER".equals(userType)) {
                return "redirect:/it/dashboard";
            } else if ("ADMIN".equals(userType)) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/home";
            }
        }
        return "login";
    }


    // ================= REGISTER =================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register"; // maps to register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO dto, Model model) {
        // password check
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Passwords do not match!");
            return "register";
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // hash password
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // default userType = PASSENGER
        String userType = (dto.getUserType() == null || dto.getUserType().isEmpty())
                ? "PASSENGER"
                : dto.getUserType();
        user.setUserType(userType);

        try {
            // 1️⃣ Save user in Users table
            userService.createUser(user);

            // 2️⃣ If passenger, also save in Passenger table
            if ("PASSENGER".equalsIgnoreCase(userType)) {
                Passenger passenger = new Passenger();

                // link passenger to user
                passenger.setUserId(user.getUserId());

                // address from DTO (or default to "N/A")
                passenger.setAddress(dto.getAddress() != null ? dto.getAddress() : "N/A");

                // generate passengerCode (P1, P2...)
                passengerService.createPassenger(passenger);
            }

            model.addAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

}
