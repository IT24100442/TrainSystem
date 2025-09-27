package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String passengerDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (!isPassenger(loggedInUser)) {
            return "redirect:/login"; // block unauthorized access
        }

        // Get passenger record; create if not exists
        Passenger passenger = passengerService.getPassengerById(loggedInUser.getUserId());
        if (passenger == null) {
            passenger = new Passenger();
            passenger.setUserId(loggedInUser.getUserId());
            passengerService.createPassenger(passenger);
        }

        model.addAttribute("userName", loggedInUser.getName());
        model.addAttribute("userEmail", loggedInUser.getEmail());
        model.addAttribute("passenger", passenger);

        return "passenger/dashboard"; // maps to passenger dashboard view
    }

    // ================== VIEW PASSENGER DETAILS ==================
    @GetMapping("/view")
    public String viewPassengerDetails(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (!isPassenger(loggedInUser)) {
            return "redirect:/login";
        }

        Passenger passenger = passengerService.getPassengerById(loggedInUser.getUserId());
        model.addAttribute("passenger", passenger);
        return "passenger/users";
    }

    // ================== EDIT PASSENGER PROFILE ==================
    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (!isPassenger(loggedInUser)) {
            return "redirect:/login";
        }

        Passenger passenger = passengerService.getPassengerById(loggedInUser.getUserId());
        model.addAttribute("passenger", passenger);
        return "passenger/edit";
    }

    @PostMapping("/update")
    public String updatePassenger(@ModelAttribute Passenger updated, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (!isPassenger(loggedInUser)) {
            return "redirect:/login";
        }

        passengerService.updatePassengerDetails(loggedInUser.getUserId(), updated.getAddress());
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

    // ================== HELPER METHOD ==================
    private boolean isPassenger(User user) {
        return user != null && user.getUserType() != null &&
                user.getUserType().trim().equalsIgnoreCase("PASSENGER");
    }
}
