package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // Redirect logged-in users to their dashboard
            if ("PASSENGER".equalsIgnoreCase(loggedInUser.getUserType())) {
                return "redirect:/passenger/dashboard";
            }
            // add other user types if needed
        }
        return "home"; // landing page home.html
    }

}