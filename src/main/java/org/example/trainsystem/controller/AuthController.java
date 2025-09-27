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


}
