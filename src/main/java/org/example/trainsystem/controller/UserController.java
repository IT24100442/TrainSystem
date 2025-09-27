package org.example.trainsystem.controller;


import org.example.trainsystem.auth.PWEncoder;
import org.example.trainsystem.dto.UserDTO;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.PassengerDAO;
import org.example.trainsystem.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private final UserDAO userDAO;

    @Autowired
    private PassengerDAO passengerDAO;


    PWEncoder pwEncoder;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Show registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO()); // DTO to hold form data
        return "it/register";
    }

    // Handle form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO user, Model model) {
        // Validate passwords
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "it/register";
        }
        User user2 = new User();
        user2.setUsername(user.getUsername());

        String hashedPassword = pwEncoder.encode(user.getPassword()); //
        user2.setPassword(hashedPassword);
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setUserType(user.getUserType());

        System.out.println("user type: :+ " + user2.getUserType());


        // Save to DB
        userDAO.save(user2);
        // Example: save user (you’d normally call a service here)
        System.out.println("New user registered: " + user);
        int userId = user2.getUserId();

        if(user.getUserType().equals("Passenger")) {
            Passenger passenger = new Passenger();
            passenger.setAddress(user.getAddress());
            passenger.setUserId(userId);
            try{
                passengerDAO.save(passenger);
                System.out.println("New passenger registered: " + passenger);
            }catch (Exception e){
                System.out.println("Error saving passenger: " + e.getMessage());
                model.addAttribute("error", "Error saving passenger details.");
                return "register-passenger";
            }

        }
        switch (user.getUserType()) {
            case "ItOfficer":
                return "redirect:/it/register?userId=" + userId;
//            case "Passenger":
//                return "redirect:/passenger/registerAddress?userId=" + userId;
            case "Admin":
                return "redirect:/register/admin?userId=" + userId;
            default:
                return "redirect:/login";
        }
    }
}
