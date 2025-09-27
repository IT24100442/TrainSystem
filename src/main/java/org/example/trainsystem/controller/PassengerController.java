package org.example.trainsystem.controller;

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

    @PostMapping("/update")
    public String updatePassenger(@ModelAttribute Passenger updated, Authentication authentication, Model model) {
        String username = authentication.getName();
        Passenger passenger = passengerService.getPassengerWithUser(username);

        if (passenger == null) {
            return "redirect:/login";
        }

        passengerService.updatePassengerDetails(passenger.getUserId(), updated.getAddress());
        model.addAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/passenger/dashboard";
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
