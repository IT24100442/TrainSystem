package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Booking;
import org.example.trainsystem.entity.TicketOfficer;
import org.example.trainsystem.repository.BookingDAO;
import org.example.trainsystem.repository.TicketOfficerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/Ticket_Officer")
public class TicketOfficerController {

    @Autowired
    private TicketOfficerDAO ticketOfficerDAO;


    @Autowired
    private BookingDAO bookingDAO;

    public TicketOfficerController(TicketOfficerDAO ticketOfficerDAO) {
        this.ticketOfficerDAO = ticketOfficerDAO;
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        // Fetch driver info using username
        TicketOfficer ticketOfficer = ticketOfficerDAO.findTicketOfficerWithUser(username);

        int ticketOfficerId = ticketOfficer.getUserId();
        int trainId = ticketOfficer.getTrainId();

        System.out.println();
        System.out.println("Ticket Officer ID: " + ticketOfficerId);
        System.out.println("Assigned Train ID: " + trainId);
        System.out.println();


        List<Booking> bookings = bookingDAO.findByTrainId(trainId);
        model.addAttribute("userId", ticketOfficerId);
        model.addAttribute("bookings", bookings);


        return "ticket/dashboard"; // Thymeleaf template
    }

    @GetMapping("/violation-report")  // Fixed: removed duplicate path
    public String violationReport(@RequestParam(required = false) int bookingId,
                                  @RequestParam(required = false) int trainNumber,
                                  @RequestParam(required = false) String seatNumber,
                                  @RequestParam(required = false) int passengerId,
                                  Model model) {


        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        // Fetch driver info using username
        TicketOfficer ticketOfficer = ticketOfficerDAO.findTicketOfficerWithUser(username);

        int ticketOfficerId = ticketOfficer.getUserId();
        model.addAttribute("ticketOfficerId", ticketOfficerId);


        // Add parameters to model (with defaults if null)
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("trainNumber", trainNumber);
        model.addAttribute("seatNumber", seatNumber != null ? seatNumber : "");
        model.addAttribute("passengerId", passengerId);

        // Add current date and time
        model.addAttribute("currentDate", LocalDate.now().toString());
        model.addAttribute("currentTime", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        return "ticket/Violation"; // Returns violation-report.html template
    }

}