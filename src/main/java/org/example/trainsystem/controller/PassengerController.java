package org.example.trainsystem.controller;


import org.example.trainsystem.dto.UserDTO;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.repository.PassengerDAO;
import org.example.trainsystem.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Collections;
import java.util.Map;

import java.security.Principal;

@Controller
public class PassengerController {

    @Autowired
    PassengerDAO passengerDAO;

    @Autowired
    PassengerService passengerService;

    @GetMapping("/registration")
    public String showPassengerRegistrationForm(Model model) {
        UserDTO user = new UserDTO();
        user.setUserType("Passenger"); // default for passenger registration
        model.addAttribute("user", user);
        return "register-passenger";
    }

    @PostMapping("/registration")
    public String registerPassenger(@ModelAttribute("user") UserDTO userDTO,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        try {
            // Check if username already exists
            if (passengerService.usernameExists(userDTO.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Username already exists! Please choose a different username.");
                model.addAttribute("user", userDTO);
                return "redirect:/registration";
            }

            // Check if email already exists
            if (passengerService.emailExists(userDTO.getEmail())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Email already registered! Please use a different email or log in.");
                model.addAttribute("user", userDTO);
                return "redirect:/registration";
            }

            // Register the passenger using your service
            passengerService.registerPassenger(userDTO);

            // Add success message
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please log in.");

            // Redirect to login page
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error during registration: " + e.getMessage());
            return "redirect:/registration";
        }
    }


    @GetMapping("/passenger/dashboard")
    public String showPassengerDashboard(Model model, Principal principal) {
        // Get the logged-in passenger
        if (principal != null) {
            Passenger passenger = passengerService.findByUsername(principal.getName());
            model.addAttribute("passenger", passenger);
        }
        return "passenger/dashboard";
    }

    // Display edit account page
    @GetMapping("/account/edit")
    public String showEditAccount(Model model, Principal principal) {
        if (principal != null) {
            Passenger passenger = passengerService.findByUsername(principal.getName());
            model.addAttribute("passenger", passenger);
        }
        return "passenger/edit-account";
    }

    // Update personal information
    @PostMapping("/account/update")
    public String updateAccount(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String address,
                                @RequestParam(required = false) String phone,
                                RedirectAttributes redirectAttributes,
                                Principal principal) {
        try {
            // Update User fields (name, email) and Passenger fields (address)
            passengerService.updatePassenger(principal.getName(), name, email, address);
            redirectAttributes.addFlashAttribute("message",
                    "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error updating profile: " + e.getMessage());
        }
        return "redirect:/account/edit";
    }

    // Change password
    @PostMapping("/account/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        try {
            // Validate passwords match
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "New passwords do not match!");
                return "redirect:/account/edit";
            }

            passengerService.changePassword(principal.getName(),
                    currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("message",
                    "Password changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error changing password: " + e.getMessage());
        }
        return "redirect:/account/edit";
    }

    // Delete account (optional - be careful with this!)
    @PostMapping("/account/delete")
    public String deleteAccount(Principal principal, RedirectAttributes redirectAttributes) {
        try {
            passengerService.deleteAccount(principal.getName());
            // Logout after deletion
            SecurityContextHolder.clearContext();
            return "redirect:/logout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting account: " + e.getMessage());
            return "redirect:/account/edit";
        }
    }

    @GetMapping("/api/check-username")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam String username) {
        boolean exists = passengerService.usernameExists(username);
        return Collections.singletonMap("exists", exists);
    }

    @GetMapping("/api/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean exists = passengerService.emailExists(email);
        return Collections.singletonMap("exists", exists);
    }
}