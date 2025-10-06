package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Booking;
import org.example.trainsystem.entity.Train;
import org.example.trainsystem.entity.TrainRoute;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private TrainRouteDAO trainRouteDAO;

    @Autowired
    private TrainDAO trainDAO;

    @Autowired
    private RouteDAO routeDAO;

    @Autowired
    private UserDAO userDAO;

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

    @GetMapping("/list-my-bookings")
    public String listBookings(Model model) {
        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        User user = userDAO.findByUsername(username);// logged in passenger
        System.out.print("userId: "+user.getUserId()+ " username: "+user.getUsername());
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user not found
        }

        List<Booking> bookings = bookingDAO.findByPassengerId(user.getUserId());
        model.addAttribute("bookings", bookings);

        return "passenger/my-bookings"; // Thymeleaf template to display bookings
    }

    @GetMapping("/train")
    public String showTrainBookingForm(Model model){

        model.addAttribute("routes", routeDAO.findAll());
//        model.addAttribute("trains", trainDAO.getAllTrains());
        return "passenger/booking";
    }

    @GetMapping("/trains/byRoute")
    @ResponseBody
    public List<Train> getTrainsByRoute(@RequestParam("routeId") int routeId) {
        return trainDAO.getTrainsByRouteId(routeId);
    }



    @PostMapping("/create")
    public String createBooking(@RequestParam int routeId,
                                @RequestParam int trainId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime travelDate,
                                RedirectAttributes redirectAttributes) {
        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        int userId = userDAO.findByUsername(username).getUserId();// logged in passenger
        Booking booking = new Booking();
        booking.setStatus("Booked");
        booking.setPassengerId(userId);
        booking.setTrainId(trainId);
        booking.setBookingDate(LocalDate.from(travelDate));
        booking.setBookingClass("Economy");
        booking.setSeatNumber("A1");


        bookingDAO.save(booking);

        int bookingId = bookingDAO.getLatestBookingId();
        System.out.println("Booking created with ID: " + bookingId);
        booking.setBookingId(bookingId);
        redirectAttributes.addFlashAttribute("booking", booking);
        return "redirect:/payment/add";
    }


}
