package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Booking;
import org.example.trainsystem.entity.Route;
import org.example.trainsystem.entity.Train;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.BookingDAO;
import org.example.trainsystem.repository.RouteDAO;
import org.example.trainsystem.repository.TrainDAO;
import org.example.trainsystem.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingTicketController {

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private TrainDAO trainDAO;

    @Autowired
    private  UserDAO userDAO;

    @Autowired
    private RouteDAO routeDAO;

    public BookingTicketController(BookingDAO bookingDAO, TrainDAO trainDAO, UserDAO userDAO) {
        this.bookingDAO = bookingDAO;
        this.trainDAO = trainDAO;
        this.userDAO = userDAO;
    }

    @GetMapping("/bookingTicket")
    public String showTicket(@RequestParam("bookingId") int bookingId, Model model) {

        // Fetch booking
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            model.addAttribute("error", "Booking not found!");
            return "error-page";
        }

        if (booking.getBookingStatus().equals("Pending")) {
            model.addAttribute("error", "Booking is still pending. Please complete payment to view the ticket.");
            return "error-page";
        }

        // Fetch train info
        Train train = trainDAO.getTrainById(booking.getTrainId());
        if (train != null) {
            booking.setTrainName(train.getName());
        }

        // Fetch passenger info
        User passenger = userDAO.findById(booking.getPassengerId());
        if (passenger != null) {
            booking.setPassengerName(passenger.getName());
        }

        Route route = routeDAO.getRouteByTrainId(booking.getTrainId());
        if (route != null) {
            booking.setRouteName(route.getRouteName());
            booking.setBookingTime(route.getAvailableTime());
        }

        // Populate seat numbers (simulate fetching from booking or trigger logic)
        List<String> seatNumbers = new ArrayList<>();
        for (int i = 1; i <= booking.getNumberOfSeats(); i++) {
            String prefix = switch (booking.getBookingClass()) {
                case "First" -> "A";
                case "Second" -> "B";
                case "Third" -> "C";
                default -> "X";
            };
            seatNumbers.add(prefix + i);
        }
        booking.setSeats(seatNumbers);

        // Calculate total amount
        double classPrice = switch (booking.getBookingClass()) {
            case "First" -> 2500.0;
            case "Second" -> 1500.0;
            case "Third" -> 800.0;
            default -> 0.0;
        };
        booking.setTotalAmount(classPrice * booking.getNumberOfSeats());

        // Add booking to model
        model.addAttribute("booking", booking);

        return "passenger/booking-ticket"; // Thymeleaf template
    }
}
