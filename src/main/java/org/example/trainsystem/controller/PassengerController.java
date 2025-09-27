package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    // ================== PASSENGER DASHBOARD ==================
    @GetMapping("/dashboard")
    public String passengerDashboard(Authentication authentication, Model model) {
        String username = authentication.getName(); // logged-in username

        Passenger passenger = passengerService.getPassengerWithUser(username);
        if (passenger == null) {
            return "redirect:/login"; // safety check
        }

        model.addAttribute("userName", passenger.getName());
        model.addAttribute("userEmail", passenger.getEmail());
        model.addAttribute("passenger", passenger);

        return "passenger/dashboard";
    }

    // ================== VIEW PASSENGER DETAILS ==================
    @GetMapping("/view")
    public String viewPassengerDetails(Authentication authentication, Model model) {
        String username = authentication.getName();
        Passenger passenger = passengerService.getPassengerWithUser(username);

        if (passenger == null) {
            return "redirect:/login";
        }

        model.addAttribute("passenger", passenger);
        return "passenger/users";
    }

    // ================== EDIT PASSENGER PROFILE ==================
    @GetMapping("/edit")
    public String showEditForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        Passenger passenger = passengerService.getPassengerWithUser(username);

        if (passenger == null) {
            return "redirect:/login";
        }

        model.addAttribute("passenger", passenger);
        return "passenger/edit";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("passenger") Passenger formPassenger,
                                HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Set the correct userId to ensure the correct passenger is updated
        formPassenger.setUserId(userId);

        // Update full profile via service
        passengerService.updatePassengerProfile(formPassenger);

        // Reload passenger data to reflect changes in the dashboard
        Passenger updatedPassenger = passengerService.getPassengerById(userId);
        model.addAttribute("passenger", updatedPassenger);
        model.addAttribute("successMessage", "Profile updated successfully!");

        return "passenger-dashboard"; // Your Thymeleaf template
    }


    // ================== LIST ALL PASSENGERS (ADMIN USE CASE) ==================
    @GetMapping("/list")
    public String listPassengers(Model model) {
        List<Passenger> passengers = passengerService.getAllPassengers();
        model.addAttribute("passengers", passengers);
        return "passenger/list";
    }

    // ================== SEARCH PASSENGERS (ADMIN USE CASE) ==================
    @GetMapping("/search")
    public String searchPassengers(@RequestParam(required = false) String address, Model model) {
        List<Passenger> passengers;

        if (address != null && !address.trim().isEmpty()) {
            passengers = passengerService.searchPassengersByAddress(address.trim());
        } else {
            passengers = passengerService.getAllPassengers();
        }

        model.addAttribute("passengers", passengers);
        model.addAttribute("searchAddress", address);
        return "passenger/search";
    }
}
