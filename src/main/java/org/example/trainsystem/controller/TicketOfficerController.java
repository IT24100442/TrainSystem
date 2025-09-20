package org.example.trainsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/Ticket_Officer")
public class TicketOfficerController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "ticket_officer_dashboard"; // Thymeleaf template
    }

    @GetMapping("/Ticket_Officer/violation-report")
    public String violationReport(@RequestParam String bookingId,
                                  @RequestParam String passengerName,
                                  @RequestParam String trainNumber,
                                  @RequestParam String seatNumber,
                                  @RequestParam String officerName,
                                  Model model) {
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("passengerName", passengerName);
        model.addAttribute("trainNumber", trainNumber);
        model.addAttribute("seatNumber", seatNumber);
        model.addAttribute("officerName", officerName);

        return "violation-report"; // Returns violation-report.html template
    }
    }

