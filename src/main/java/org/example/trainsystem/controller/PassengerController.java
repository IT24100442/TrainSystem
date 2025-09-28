package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

        // Add passenger data to model
        model.addAttribute("passenger", passenger);

        // Add dashboard statistics (you can implement these methods in your service)
        model.addAttribute("activeTicketsCount", getActiveTicketsCount(passenger));
        model.addAttribute("completedTripsCount", getCompletedTripsCount(passenger));
        model.addAttribute("openComplaintsCount", getOpenComplaintsCount(passenger));

        // Add collections for dashboard sections (implement these in your service)
        model.addAttribute("tickets", getPassengerTickets(passenger));
        model.addAttribute("travelHistory", getTravelHistory(passenger));
        model.addAttribute("notifications", getNotifications(passenger));
        model.addAttribute("complaints", getComplaints(passenger));

        // Add a booking form object for the quick book section
        model.addAttribute("bookingForm", new BookingForm());

        return "passenger/dashboard"; // This should map to your dashboard.html
    }

    // ================== HELPER METHODS (Implement these based on your business logic) ==================

    private int getActiveTicketsCount(Passenger passenger) {
        // Implement logic to count active tickets
        return 0; // Replace with actual implementation
    }

    private int getCompletedTripsCount(Passenger passenger) {
        // Implement logic to count completed trips
        return 0; // Replace with actual implementation
    }

    private int getOpenComplaintsCount(Passenger passenger) {
        // Implement logic to count open complaints
        return 0; // Replace with actual implementation
    }

    private List<?> getPassengerTickets(Passenger passenger) {
        // Implement logic to get passenger tickets
        return Collections.emptyList(); // Replace with actual implementation
    }

    private List<?> getTravelHistory(Passenger passenger) {
        // Implement logic to get travel history
        return Collections.emptyList(); // Replace with actual implementation
    }

    private List<?> getNotifications(Passenger passenger) {
        // Implement logic to get notifications
        return Collections.emptyList(); // Replace with actual implementation
    }

    private List<?> getComplaints(Passenger passenger) {
        // Implement logic to get complaints
        return Collections.emptyList(); // Replace with actual implementation
    }

    // ================== BOOKING FORM ==================
    @PostMapping("/search-trains")
    public String searchTrains(@ModelAttribute BookingForm bookingForm, Model model) {
        // Implement train search logic
        model.addAttribute("searchResults", Collections.emptyList());
        return "passenger/search-results";
    }

    @PostMapping("/book-ticket")
    public String bookTicket(@ModelAttribute BookingForm bookingForm, Authentication authentication) {
        String username = authentication.getName();
        // Implement ticket booking logic
        return "redirect:/passenger/dashboard";
    }

    // ================== REST OF YOUR EXISTING METHODS ==================

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
                                Authentication authentication, Model model) {
        String username = authentication.getName();
        Passenger currentPassenger = passengerService.getPassengerWithUser(username);

        if (currentPassenger == null) {
            return "redirect:/login";
        }

        // Set the correct userId to ensure the correct passenger is updated
        formPassenger.setUserId(currentPassenger.getUserId());

        // Update full profile via service
        passengerService.updatePassengerProfile(formPassenger);

        model.addAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/passenger/dashboard";
    }

    @GetMapping("/list")
    public String listPassengers(Model model) {
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

    // ================== BOOKING FORM CLASS ==================
    public static class BookingForm {
        private String fromStation;
        private String toStation;
        private String travelDate;
        private int passengerCount = 1;
        private String ticketClass = "economy";

        // Getters and setters
        public String getFromStation() { return fromStation; }
        public void setFromStation(String fromStation) { this.fromStation = fromStation; }

        public String getToStation() { return toStation; }
        public void setToStation(String toStation) { this.toStation = toStation; }

        public String getTravelDate() { return travelDate; }
        public void setTravelDate(String travelDate) { this.travelDate = travelDate; }

        public int getPassengerCount() { return passengerCount; }
        public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }

        public String getTicketClass() { return ticketClass; }
        public void setTicketClass(String ticketClass) { this.ticketClass = ticketClass; }
    }
}