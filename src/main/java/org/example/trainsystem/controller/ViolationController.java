package org.example.trainsystem.controller;

import org.example.trainsystem.dto.ViolationReportRequest;
import org.example.trainsystem.entity.ViolationReport;
import org.example.trainsystem.service.TicketOfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/violations")
public class ViolationController {

    @Autowired
    private TicketOfficerService ticketOfficerService;

    // Report a new violation
    @PostMapping("/report")
    public ResponseEntity<String> reportViolation(@RequestBody ViolationReportRequest request) {
        try {
            ticketOfficerService.reportViolation(
                    request.getOfficerId(),
                    request.getTrainId(),
                    request.getPassengerId(),
                    request.getViolationType(),
                    request.getViolationDescription(),
                    BigDecimal.valueOf(request.getPenaltyAmount())
            );
            return ResponseEntity.ok("Violation reported successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to report violation: " + e.getMessage());
        }
    }

    // Get all violations reported by a specific officer
    @GetMapping("/officer/{officerId}")
    public ResponseEntity<List<ViolationReport>> getOfficerViolations(@PathVariable String officerId) {
        try {
            List<ViolationReport> violations = ticketOfficerService.getOfficerViolations(officerId);
            return ResponseEntity.ok(violations);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get details of a specific violation
    @GetMapping("/{violationId}")
    public ResponseEntity<ViolationReport> getViolationDetails(@PathVariable Integer violationId) {
        try {
            ViolationReport violation = ticketOfficerService.getViolationDetails(violationId);
            if (violation == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(violation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // (Future) Get all pending violations
    @GetMapping("/pending")
    public ResponseEntity<List<ViolationReport>> getPendingViolations() {
        try {
            // TODO: Implement in TicketOfficerService
            // List<ViolationReport> pendingViolations = ticketOfficerService.getPendingViolations();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
