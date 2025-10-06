package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Booking;
import org.example.trainsystem.entity.Payment;
import org.example.trainsystem.repository.PaymentDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/payment")
public class PaymentController {



    private final PaymentDAO paymentDAO;

    public PaymentController(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @GetMapping("/add")
    public String showAddPaymentForm(@ModelAttribute("booking") Booking booking , Model model) {
        return "passenger/payment-add"; // create this Thymeleaf page
    }

    @PostMapping("/add")
    public String addPayment(@RequestParam("bookingId") int bookingId,
                             @RequestParam("amount") double amount,
                             @RequestParam("bookingClass") String bookingClass,
                             @RequestParam("paymentMethod") String paymentMethod,
                             RedirectAttributes redirectAttributes) {

        double minAmount;
        switch (bookingClass.toLowerCase()) {
            case "first":
                minAmount = 2500.0;
                break;
            case "second":
                minAmount = 1500.0;
                break;
            case "economy":
                minAmount = 800.0;
                break;
            default:
                redirectAttributes.addFlashAttribute("error", "Invalid booking class.");
                return "redirect:/payment/add?bookingId=" + bookingId;
        }

        if (amount < minAmount) {
            redirectAttributes.addFlashAttribute("error",
                    "Amount is too low for " + bookingClass + " class. Minimum: " + minAmount);
            return "redirect:/payment/add?bookingId=" + bookingId;
        }

        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now()); // Set payment date automatically

        try{
            paymentDAO.save(payment);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());;
            redirectAttributes.addFlashAttribute("error", "Error processing payment. Please try again.");
            return "redirect:/payment/add?bookingId=" + bookingId;
        }

        redirectAttributes.addFlashAttribute("success", "Payment completed successfully!");
        return "redirect:/booking/list-my-bookings"; // Redirect after successful payment
    }



}
