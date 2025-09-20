package org.example.trainsystem.service;

import org.example.trainsystem.entity.*;
import org.example.trainsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketOfficerService {

    @Autowired
    private TicketOfficerDAO ticketOfficerDAO;

    @Autowired
    private ViolationReportDAO violationReportDAO;

    @Autowired
    private RouteDAO routeDAO;

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private TrainDAO trainDAO;

    // -------- Ticket Officer --------
    public TicketOfficer getTicketOfficerWithUser(String username) {
        TicketOfficer ticketOfficer = ticketOfficerDAO.findTicketOfficerWithUser(username);
        if (ticketOfficer == null) {
            throw new RuntimeException("Ticket Officer not found with username: " + username);
        }
        return ticketOfficer;
    }

    public Route getOfficerRoute(String officerId) {
        return routeDAO.findByOfficerId(officerId);
    }

    // -------- Train & Passenger --------
    public List<Booking> getPassengerList(String trainId) {
        return bookingDAO.findByTrainId(trainId);
    }

    public Train getCurrentTrain(String routeId) {
        return trainDAO.findByRouteId(routeId);
    }

    // -------- Violations --------
    public void reportViolation(String officerId,
                                String trainId,
                                String passengerId,
                                String violationType,
                                String violationDescription,
                                BigDecimal penaltyAmount) {

        ViolationReport violationReport = new ViolationReport(
                officerId,
                trainId,
                passengerId,
                violationType,
                violationDescription,
                LocalDateTime.now()
        );
        violationReport.setPenaltyAmount(penaltyAmount);
        violationReport.setReportStatus("PENDING");

        int result = violationReportDAO.save(violationReport);
        if (result == 0) {
            throw new RuntimeException("Failed to report violation");
        }
    }

    public List<ViolationReport> getOfficerViolations(String officerId) {
        return violationReportDAO.findByOfficerId(officerId);
    }

    public ViolationReport getViolationDetails(Integer violationId) {
        return violationReportDAO.findViolationReportWithDetails(violationId)
                .orElseThrow(() -> new RuntimeException("Violation not found with ID: " + violationId));
    }

    // -------- Ticket Verification --------
    public void verifyTicket(String bookingId, String status) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }

        booking.setTicketStatus(status);
        int result = bookingDAO.update(booking);
        if (result == 0) {
            throw new RuntimeException("Failed to verify ticket");
        }
    }
}
