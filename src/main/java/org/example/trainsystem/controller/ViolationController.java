package org.example.trainsystem.controller;

import org.example.trainsystem.entity.TicketOfficer;
import org.example.trainsystem.entity.ViolationReport;
import org.example.trainsystem.repository.TicketOfficerDAO;
import org.example.trainsystem.repository.ViolationReportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/violation")
public class ViolationController {


    @Autowired
    private TicketOfficerDAO ticketOfficerDAO;

    @Autowired
    private ViolationReportDAO violationReportDAO;

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

    @PostMapping("/submit")
    public String submitViolation(
            @RequestParam("passengerId") int passengerId,
            @RequestParam("trainNumber") int trainNumber,
            @RequestParam("violationType") String violationType,
            RedirectAttributes redirectAttributes) {

        // ✅ Log or save the violation
        System.out.println("Violation submitted:");
        System.out.println("Passenger ID: " + passengerId);
        System.out.println("Train Number: " + trainNumber);
        System.out.println("Violation Type: " + violationType);

        String username = getAuthenticatedUsername();
        TicketOfficer ticketOfficer = ticketOfficerDAO.findTicketOfficerWithUser(username);

        int ticketOfficerId = ticketOfficer.getUserId();
        ViolationReport violationReport = new ViolationReport();
        violationReport.setTicketOfficerId(ticketOfficerId);

        System.out.println("Ticket Officer ID: " + ticketOfficerId);
        violationReport.setPassengerId(passengerId);
        violationReport.setTrainId(trainNumber);
        violationReport.setViolationType(violationType);

        violationReportDAO.save(violationReport);


        // Add confirmation to the model
        redirectAttributes.addFlashAttribute("message", "Violation report saved successfully!");        ;

        // Redirect or show a confirmation page
        return "redirect:/Ticket_Officer/dashboard";
    }

}