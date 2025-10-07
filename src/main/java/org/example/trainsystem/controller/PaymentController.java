package org.example.trainsystem.controller;

import org.example.trainsystem.entity.*;
import org.example.trainsystem.repository.BookingDAO;
import org.example.trainsystem.repository.PaymentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/payment")
public class PaymentController {


    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private BookingDAO bookingDAO;



    public PaymentController(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @GetMapping("/add")
    public String showAddPaymentForm(@ModelAttribute("booking") Booking booking , Model model) {
        double pricePerSeat;
        switch (booking.getBookingClass()) {
            case "First":
                pricePerSeat = 2500.0;
                break;
            case "Second":
                pricePerSeat = 1500.0;
                break;
            case "Third":
                pricePerSeat = 800.0;
                break;
            default:
                pricePerSeat = 0.0;
        }

        double totalAmount = pricePerSeat * booking.getNumberOfSeats();
        model.addAttribute("booking", booking);
        model.addAttribute("totalAmount", totalAmount);

        return "passenger/payment-add";
    }

    @PostMapping("/add")
    public String addPayment(@RequestParam("bookingId") int bookingId,
                             @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam(required = false) String cardNumber,
                             @RequestParam(required = false) String cardHolder,
                             @RequestParam(required = false) String expiryDate,
                             @RequestParam(required = false) String cvv,
                             @RequestParam(required = false) String mobileNumber,
                             RedirectAttributes redirectAttributes) {

        // Fetch the booking
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            redirectAttributes.addFlashAttribute("error", "Booking not found.");
            return "redirect:/booking/list-my-bookings";
        }

        // Determine price per seat based on class
        double pricePerSeat;
        switch (booking.getBookingClass()) {
            case "First":
                pricePerSeat = 2500.0;
                break;
            case "Second":
                pricePerSeat = 1500.0;
                break;
            case "Third":
                pricePerSeat = 800.0;
                break;
            default:
                redirectAttributes.addFlashAttribute("error", "Invalid booking class.");
                return "redirect:/booking/list-my-bookings";
        }

        // Calculate total amount
        double totalAmount = pricePerSeat * booking.getNumberOfSeats();

        PaymentContext context = new PaymentContext();

        switch (paymentMethod.toLowerCase()) {
            case "card":
                context.setPaymentStrategy(new CreditCardPayment(cardNumber, cardHolder, expiryDate, cvv));
                break;
            case "mobile":
                context.setPaymentStrategy(new MobilePayment(mobileNumber));
                break;
            default:
                System.out.println("Invalid payment type!");
                redirectAttributes.addFlashAttribute("error", "Invalid payment method.");
                return "redirect:/payment/add?bookingId=" + bookingId;


        }
        String paymentResult = context.executePayment(totalAmount);


        // Save payment
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(totalAmount);
        payment.setPaymentDate(LocalDateTime.now());


        try {
            paymentDAO.save(payment);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error processing payment. Please try again.");
            return "redirect:/payment/add?bookingId=" + bookingId;
        }

        redirectAttributes.addFlashAttribute("success", paymentResult);
        return "redirect:/booking/list-my-bookings";
    }




}
