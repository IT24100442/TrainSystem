package org.example.trainsystem.controller;


import org.example.trainsystem.dto.UserDTO;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.repository.PassengerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PassengerController {

    @Autowired
    PassengerDAO passengerDAO;

    @GetMapping("/registration")
    public String showPassengerRegistrationForm(Model model) {
        UserDTO user = new UserDTO();
        user.setUserType("Passenger"); // default for passenger registration
        model.addAttribute("user", user);
        return "register-passenger";

    }

    @GetMapping("/passenger/dashboard")
    public String showPassengerDashboard() {
        return "passenger/dashboard";
    }

}
