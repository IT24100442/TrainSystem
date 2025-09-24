package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    // ================= PASSENGER DASHBOARD =================
    @GetMapping("/home")
    public String passengerHome(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"PASSENGER".equalsIgnoreCase(loggedInUser.getUserType())) {
            return "redirect:/login"; // prevent unauthorized access
        }

        Passenger passenger = passengerService.getPassengerById(Integer.valueOf(loggedInUser.getUserId()));
        model.addAttribute("userName", loggedInUser.getName());
        model.addAttribute("userEmail", loggedInUser.getEmail());
        model.addAttribute("passenger", passenger);

        return "passenger"; // maps to passenger.html dashboard
    }

    // ================= VIEW DETAILS =================
    @GetMapping("/{userId}")
    public String viewPassengerDetails(@PathVariable String userId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !userId.equals(loggedInUser.getUserId())) {
            return "redirect:/login"; // only allow logged-in passenger
        }

        Passenger passenger = passengerService.getPassengerById(Integer.parseInt(userId));
        if (passenger == null) {
            model.addAttribute("errorMessage", "Passenger not found");
            return "error";
        }
        model.addAttribute("passenger", passenger);
        return "passenger/details";
    }

    // ================= LIST & SEARCH =================
    @GetMapping("/list")
    public String listActivePassengers(Model model) {
        List<Passenger> passengers = passengerService.getAllPassengers();
        model.addAttribute("passengers", passengers);
        return "passenger/list";
    }

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

    // ================= EDIT / UPDATE =================
    @GetMapping("/edit/{userId}")
    public String showEditForm(@PathVariable String userId, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !userId.equals(loggedInUser.getUserId())) {
            return "redirect:/login";
        }

        Passenger passenger = passengerService.getPassengerById(Integer.parseInt(userId));
        if (passenger == null) {
            model.addAttribute("errorMessage", "Passenger not found");
            return "error";
        }
        model.addAttribute("passenger", passenger);
        return "passenger/edit";
    }

    @PostMapping("/api/passengers/update")
    @ResponseBody
    public String updateCurrentPassenger(@RequestBody Passenger updated, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "Unauthorized";

        passengerService.updatePassengerDetails(String.valueOf(loggedInUser.getUserId()), updated.getAddress());
        // also update name/email in User entity if needed
        return "Profile updated successfully!";
    }


    // ========================== API ENDPOINTS ==========================
    @GetMapping("/api/{userId}")
    @ResponseBody
    public Passenger getPassengerApi(@PathVariable String userId) {
        return passengerService.getPassengerById(Integer.parseInt(userId));
    }

    @GetMapping("/api/search/address/{address}")
    @ResponseBody
    public List<Passenger> searchByAddressApi(@PathVariable String address) {
        return passengerService.searchPassengersByAddress(address);
    }

    @GetMapping("/api/active")
    @ResponseBody
    public List<Passenger> getActivePassengersApi() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/api/exists/{userId}")
    @ResponseBody
    public boolean checkPassengerExistsApi(@PathVariable String userId) {
        return passengerService.passengerExists(userId);
    }

    @GetMapping("/api/count")
    @ResponseBody
    public int getPassengerCountApi() {
        return passengerService.getTotalPassengerCount();
    }

    // ========================== CURRENT LOGGED-IN PASSENGER ==========================
    // multiple user
    @GetMapping("/api/current")
    @ResponseBody
    public Passenger getCurrentPassengerApi(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"PASSENGER".equalsIgnoreCase(loggedInUser.getUserType())) {
            throw new RuntimeException("Unauthorized or not a passenger");
        }
        return passengerService.getPassengerById(Integer.valueOf(loggedInUser.getUserId()));
    }


}
