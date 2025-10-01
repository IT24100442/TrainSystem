package org.example.trainsystem.service;

import org.example.trainsystem.entity.Booking;
import org.example.trainsystem.entity.Route;
import org.example.trainsystem.entity.TicketOfficer;
import org.example.trainsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    // -------- Train & Passenger --------
    public List<Booking> getPassengerList(int trainId) {
        return bookingDAO.findByTrainId(trainId);
    }

}

//    public Train getCurrentTrain(String routeId) {
//        return trainDAO.findByRouteId(routeId);
//    }

//    // -------- Violations --------
//    public void reportViolation(int officerId,
//                                int trainId,
//                                int passengerId,
//                                String violationType
//                                ) {
//
//        ViolationReport violationReport = new ViolationReport(
//                officerId,
//                trainId,
//                passengerId,
//                violationType,
//                LocalDateTime.now()
//        );
//        violationReport.setPenaltyAmount(penaltyAmount);
//        violationReport.setReportStatus("PENDING");
//
//        int result = violationReportDAO.save(violationReport);
//        if (result == 0) {
//            throw new RuntimeException("Failed to report violation");
//        }
//    }

//    public List<ViolationReport> getOfficerViolations(String officerId) {
//        return violationReportDAO.findByOfficerId(officerId);
//    }

//    public ViolationReport getViolationDetails(Integer violationId) {
//        return violationReportDAO.findViolationReportWithDetails(violationId)
//                .orElseThrow(() -> new RuntimeException("Violation not found with ID: " + violationId));
//    }

//    // -------- Ticket Verification --------
//    public void verifyTicket(int bookingId, String status) {
//        Booking booking = bookingDAO.findById(bookingId);
//        if (booking == null) {
//            throw new RuntimeException("Booking not found with ID: " + bookingId);
//        }
//
//        booking.setTicketStatus(status);
//        int result = bookingDAO.update(booking);
//        if (result == 0) {
//            throw new RuntimeException("Failed to verify ticket");
//        }
//    }
//}
