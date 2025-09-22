package org.example.trainsystem.controller;


import org.example.trainsystem.auth.PWEncoder;
import org.example.trainsystem.dto.UserDTO;
import org.example.trainsystem.entity.User;
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
            return "register";
        }
        User user2 = new User();
        user2.setUsername(user.getUsername());

        String hashedPassword = pwEncoder.encode(user.getPassword()); // TODO: hash the password
        user2.setPassword(hashedPassword); // consider hashing later
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setUserType(user.getUserType());

        // Save to DB
        userDAO.save(user2);
        // Example: save user (you’d normally call a service here)
        System.out.println("New user registered: " + user);
        int userId = user2.getUserId();


        switch (user.getUserType()) {
            case "ItOfficer":
                return "redirect:/it/register?userId=" + userId;
            case "Customer":
                return "redirect:/register/customer?userId=" + userId;
            case "Admin":
                return "redirect:/register/admin?userId=" + userId;
            default:
                return "redirect:/login";
        }
    }
}
