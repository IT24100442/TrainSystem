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
}