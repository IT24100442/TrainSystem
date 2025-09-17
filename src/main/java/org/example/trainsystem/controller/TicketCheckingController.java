package org.example.trainsystem.controller;

import org.example.trainsystem.dto.TicketCheckingRequest;
import org.example.trainsystem.entity.Booking;
import org.example.trainsystem.entity.Route;
import org.example.trainsystem.entity.TicketOfficer;
import org.example.trainsystem.service.TicketOfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-checking")
public class TicketCheckingController {

    @Autowired
    private TicketOfficerService ticketOfficerService;

    // Get officer details by username
    @GetMapping("/officer/{username}")
    public ResponseEntity<TicketOfficer> getTicketOfficer(@PathVariable String username) {
        try {
            TicketOfficer ticketOfficer = ticketOfficerService.getTicketOfficerWithUser(username);
            return ResponseEntity.ok(ticketOfficer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get list of passengers for a train
    @GetMapping("/passengers/{trainId}")
    public ResponseEntity<List<Booking>> getPassengerList(@PathVariable String trainId) {
        try {
            List<Booking> passengers = ticketOfficerService.getPassengerList(trainId);
            return ResponseEntity.ok(passengers);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Verify a passenger's ticket
    @PostMapping("/verify")
    public ResponseEntity<String> verifyTicket(@RequestBody TicketCheckingRequest request) {
        try {
            ticketOfficerService.verifyTicket(request.getBookingId(), request.getStatus());
            return ResponseEntity.ok("Ticket verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to verify ticket: " + e.getMessage());
        }
    }

    // Get route assigned to officer
    @GetMapping("/route/{officerId}")
    public ResponseEntity<Route> getOfficerRoute(@PathVariable String officerId) {
        try {
            Route route = ticketOfficerService.getOfficerRoute(officerId);
            return ResponseEntity.ok(route);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
