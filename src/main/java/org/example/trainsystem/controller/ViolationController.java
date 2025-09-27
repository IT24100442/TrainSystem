package org.example.trainsystem.controller;

import org.example.trainsystem.dto.ViolationReportRequest;
import org.example.trainsystem.entity.ViolationReport;
import org.example.trainsystem.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller  // Changed from @RestController to @Controller
@RequestMapping("/api/violations")
public class  ViolationController {

    @Autowired
    private ViolationService violationService;

    // NEW: Handle violation report form page
    @GetMapping("/form")
    public String showViolationForm(@RequestParam(required = false) String bookingId,
                                    @RequestParam(required = false) String passengerName,
                                    @RequestParam(required = false) String trainNumber,
                                    @RequestParam(required = false) String seatNumber,
                                    @RequestParam(required = false) String officerName,
                                    Model model) {

        model.addAttribute("bookingId", bookingId != null ? bookingId : "");
        model.addAttribute("passengerName", passengerName != null ? passengerName : "");
        model.addAttribute("trainNumber", trainNumber != null ? trainNumber : "");
        model.addAttribute("seatNumber", seatNumber != null ? seatNumber : "");
        model.addAttribute("officerName", officerName != null ? officerName : "Officer");

        // Add current date and time
        model.addAttribute("currentDate", LocalDate.now().toString());
        model.addAttribute("currentTime", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        return "ticket/violation-report"; // Returns violation-report.html template
    }

//    // NEW: Handle form submission from the violation report page
//    @PostMapping("/submit")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> submitViolationReport(@RequestBody Map<String, Object> requestData) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            // Extract data from the request
//            String bookingId = (String) requestData.get("bookingId");
//            String passengerName = (String) requestData.get("passengerName");
//            String trainNumber = (String) requestData.get("trainNumber");
//            String seatNumber = (String) requestData.get("seatNumber");
//            String officerName = (String) requestData.get("officerName");
//            String violationType = (String) requestData.get("violationType");
//            String dateTime = (String) requestData.get("dateTime");
//
//            // Create ViolationReport entity
//            ViolationReport violationReport = new ViolationReport();
//            violationReport.setOfficerId(officerName);
//            violationReport.setTrainId(trainNumber);
//            violationReport.setPassengerId(bookingId);
//            violationReport.setViolationType(violationType);
//            violationReport.setViolationDescription("Violation reported for passenger: " + passengerName +
//                    " (Booking ID: " + bookingId + ")" +
//                    " on train: " + trainNumber +
//                    " at seat: " + seatNumber +
//                    " on " + dateTime);
//            violationReport.setPenaltyAmount(BigDecimal.valueOf(getDefaultPenaltyAmount(violationType)));
//            violationReport.setReportStatus("PENDING");
//            violationReport.setViolationTime(Timestamp.valueOf(LocalDateTime.now()));
//
//            // Save violation report using ViolationService
//            boolean success = violationService.createViolationReport(violationReport);
//
//            if (success) {
//                response.put("success", true);
//                response.put("message", "Violation report submitted successfully");
//                response.put("violationId", bookingId);
//            } else {
//                response.put("success", false);
//                response.put("message", "Failed to save violation report");
//            }
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Failed to submit violation report: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    // REST API endpoints using ViolationService
//
//    // Report a new violation (REST API)
//    @PostMapping("/report")
//    @ResponseBody
//    public ResponseEntity<String> reportViolation(@RequestBody ViolationReportRequest request) {
//        try {
//            // Convert DTO to Entity
//            ViolationReport violationReport = new ViolationReport();
//            violationReport.setOfficerId(request.getOfficerId());
//            violationReport.setTrainId(request.getTrainId());
//            violationReport.setPassengerId(request.getPassengerId());
//            violationReport.setViolationType(request.getViolationType());
//            violationReport.setViolationDescription(request.getViolationDescription());
//            violationReport.setPenaltyAmount(BigDecimal.valueOf(request.getPenaltyAmount()));
//            violationReport.setReportStatus(request.getReportStatus() != null ? request.getReportStatus() : "PENDING");
//            violationReport.setViolationTime(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
//
//            boolean success = violationService.createViolationReport(violationReport);
//
//            if (success) {
//                return ResponseEntity.ok("Violation reported successfully");
//            } else {
//                return ResponseEntity.badRequest().body("Failed to save violation report");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Failed to report violation: " + e.getMessage());
//        }
//    }

    // Get all violations reported by a specific officer
    @GetMapping("/officer/{officerId}")
    @ResponseBody
    public ResponseEntity<List<ViolationReport>> getOfficerViolations(@PathVariable String officerId) {
        try {
            List<ViolationReport> violations = violationService.getViolationsByOfficer(officerId);
            return ResponseEntity.ok(violations);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get details of a specific violation
//    @GetMapping("/{violationId}")
//    @ResponseBody
//    public ResponseEntity<ViolationReport> getViolationDetails(@PathVariable Integer violationId) {
//        try {
////            Optional<ViolationReport> violation = violationService.getViolationWithDetails(violationId);
//            if (violation.isPresent()) {
//                return ResponseEntity.ok(violation.get());
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Get all pending violations
    @GetMapping("/pending")
    @ResponseBody
    public ResponseEntity<List<ViolationReport>> getPendingViolations() {
        try {
            List<ViolationReport> pendingViolations = violationService.getPendingViolations();
            return ResponseEntity.ok(pendingViolations);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper method to determine default penalty amount based on violation type
    private double getDefaultPenaltyAmount(String violationType) {
        switch (violationType != null ? violationType.toLowerCase() : "") {
            case "no-ticket":
                return 500.0;
            case "wrong-seat":
                return 100.0;
            case "expired-ticket":
                return 200.0;
            case "fake-ticket":
                return 1000.0;
            case "misconduct":
                return 300.0;
            default:
                return 250.0;
        }
    }
}