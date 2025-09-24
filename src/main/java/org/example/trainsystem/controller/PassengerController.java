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

        if (loggedInUser == null || !"PASSENGER".equalsIgnoreCase(loggedInUser.getUserType())) {
            return "redirect:/login"; // block unauthorized access
        }

        Passenger passenger = passengerService.getPassengerById((loggedInUser.getUserId()));

        model.addAttribute("userName", loggedInUser.getName());
        model.addAttribute("userEmail", loggedInUser.getEmail());
        model.addAttribute("passenger", passenger);

        return "passenger/dashboard"; // maps to passenger dashboard view
    }

    // ================== VIEW PASSENGER DETAILS ==================
    @GetMapping("/view")
    public String viewPassengerDetails(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !"PASSENGER".equalsIgnoreCase(loggedInUser.getUserType())) {
            return "redirect:/login";
        }

        Passenger passenger = passengerService.getPassengerById((loggedInUser.getUserId()));
        model.addAttribute("passenger", passenger);

        return "passenger/details";
    }

    // ================== EDIT PASSENGER PROFILE ==================
    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !"PASSENGER".equalsIgnoreCase(loggedInUser.getUserType())) {
            return "redirect:/login";
        }

        Passenger passenger = passengerService.getPassengerById((loggedInUser.getUserId()));
        model.addAttribute("passenger", passenger);

        return "passenger/edit";
    }

    @PostMapping("/update")
    public String updatePassenger(@ModelAttribute Passenger updated, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Pass String userId, not int
        passengerService.updatePassengerDetails(loggedInUser.getUserId(), updated.getAddress());
        model.addAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/passenger/home"; // ✅ use /home, since your PassengerController maps dashboard there
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
