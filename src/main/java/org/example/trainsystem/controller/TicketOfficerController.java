package org.example.trainsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/Ticket_Officer")
public class TicketOfficerController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "ticket/Ticket_Officer_Dashboard"; // Thymeleaf template
    }

    @GetMapping("/violation-report")  // Fixed: removed duplicate path
    public String violationReport(@RequestParam(required = false) String bookingId,
                                  @RequestParam(required = false) String passengerName,
                                  @RequestParam(required = false) String trainNumber,
                                  @RequestParam(required = false) String seatNumber,
                                  @RequestParam(required = false) String officerName,
                                  Model model) {

        // Add parameters to model (with defaults if null)
        model.addAttribute("bookingId", bookingId != null ? bookingId : "");
        model.addAttribute("passengerName", passengerName != null ? passengerName : "");
        model.addAttribute("trainNumber", trainNumber != null ? trainNumber : "");
        model.addAttribute("seatNumber", seatNumber != null ? seatNumber : "");
        model.addAttribute("officerName", officerName != null ? officerName : "Officer");

        // Add current date and time
        model.addAttribute("currentDate", LocalDate.now().toString());
        model.addAttribute("currentTime", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        return "violation-report"; // Returns violation-report.html template
    }

    // Add this method to handle the ticket verification page
    @GetMapping("/verification")
    public String showVerification(@RequestParam(required = false) String train,
                                   Authentication authentication,
                                   Model model) {

        // Get officer name from authentication if available
        String officerName = authentication != null ? authentication.getName() : "Officer";

        model.addAttribute("officerName", officerName);
        model.addAttribute("selectedTrain", train);
        model.addAttribute("date", LocalDate.now().toString());
        model.addAttribute("time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        // Add sample statistics (replace with real data from service)
        model.addAttribute("totalPassengers", 24);
        model.addAttribute("verifiedCount", 18);
        model.addAttribute("pendingCount", 6);
        model.addAttribute("violationCount", 2);

        return "ticket-verification"; // Returns ticket-verification.html template
    }
}