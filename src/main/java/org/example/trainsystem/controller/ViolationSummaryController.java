package org.example.trainsystem.controller;

import org.example.trainsystem.entity.ViolationReport;
import org.example.trainsystem.repository.ViolationReportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/violation")
public class ViolationSummaryController {

    @Autowired
    private ViolationReportDAO violationReportDAO;

    @GetMapping({"/summary", "violation/summary"})
    public String showViolationSummary(Model model) {

        // Fetch all violations
        List<ViolationReport> violations = violationReportDAO.findAll();

        // Calculate statistics
        int totalViolations = violations.size();

        // Today's violations
        LocalDate today = LocalDate.now();
        int todayViolations = (int) violations.stream()
                .filter(v -> v.getReportedDate() != null &&
                        v.getReportedDate().toLocalDate().equals(today))
                .count();

        // Pending violations (assuming status field exists)
        int pendingViolations = (int) violations.stream()
                .filter(v -> v.getStatus() == null ||
                        v.getStatus().equalsIgnoreCase("Pending"))
                .count();

        // Resolved violations
        int resolvedViolations = (int) violations.stream()
                .filter(v -> v.getStatus() != null &&
                        v.getStatus().equalsIgnoreCase("Resolved"))
                .count();

        // Get unique train numbers for filter dropdown
        List<Integer> trains = violations.stream()
                .map(ViolationReport::getTrainId)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Add data to model
        model.addAttribute("violations", violations);
        model.addAttribute("totalViolations", totalViolations);
        model.addAttribute("todayViolations", todayViolations);
        model.addAttribute("pendingViolations", pendingViolations);
        model.addAttribute("resolvedViolations", resolvedViolations);
        model.addAttribute("trains", trains);

        return "ticket/violation-summary";
    }

    @GetMapping("/details")
    public String showViolationDetails(
            @RequestParam("violationId") int violationId,
            Model model) {

        Optional<ViolationReport> violation = violationReportDAO.findById(violationId);

        if (violation.isEmpty()) {
            model.addAttribute("error", "Violation not found");
            return "redirect:/violation/summary";
        }

        model.addAttribute("violation", violation);
        return "violation-details";
    }

    @PostMapping("/update-status")
    public String updateViolationStatus(
            @RequestParam("violationId") int violationId,
            @RequestParam("status") String status,
            Model model) {

        Optional<ViolationReport> violation = violationReportDAO.findById(violationId);

        if (violation.isPresent()) {
            violation.get().setStatus(status);
            violationReportDAO.update(violation.orElse(null));
            model.addAttribute("message", "Violation status updated successfully!");
        } else {
            model.addAttribute("error", "Violation not found");
        }

        return "redirect:/violation/summary";
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<ViolationReport> filterViolations(
            @RequestParam(required = false) String violationType,
            @RequestParam(required = false) Integer trainNumber,
            @RequestParam(required = false) String date) {

        List<ViolationReport> violations = violationReportDAO.findAll();

        // Apply filters
        if (violationType != null && !violationType.isEmpty()) {
            violations = violations.stream()
                    .filter(v -> v.getViolationType().equalsIgnoreCase(violationType))
                    .collect(Collectors.toList());
        }

        if (trainNumber != null) {
            violations = violations.stream()
                    .filter(v -> v.getTrainId() == trainNumber)
                    .collect(Collectors.toList());
        }

        if (date != null && !date.isEmpty()) {
            LocalDate filterDate = LocalDate.parse(date);
            violations = violations.stream()
                    .filter(v -> v.getReportedDate() != null &&
                            v.getReportedDate().toLocalDate().equals(filterDate))
                    .collect(Collectors.toList());
        }

        return violations;
    }

    @PostMapping("/delete")
    public String deleteViolation(
            @RequestParam("violationId") int violationId,
            Model model) {

        try {
            violationReportDAO.delete(violationId);
            model.addAttribute("message", "Violation deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete violation: " + e.getMessage());
        }

        return "redirect:/violation/summary";
    }
}