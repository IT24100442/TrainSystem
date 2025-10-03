package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Concern;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.ConcernDAO;
import org.example.trainsystem.repository.ReplyDAO;
import org.example.trainsystem.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/concern")
public class ConcernController {

    @Autowired
    private ConcernDAO concernDAO;

    @Autowired
    private ReplyDAO replyDAO;


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

    @GetMapping("/form")
    public String showConcernPage(Model model) {
        model.addAttribute("concern",new Concern());

        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        User user = userDAO.findByUsername(username);// logged in passenger
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user not found
        }

        model.addAttribute("replies", replyDAO.findByPassengerId(user.getUserId()));

        return "passenger/concern_page"; // maps to concern_page.html in templates
    }



    @PostMapping("/submit")
    public String submitConcern(Concern concern, Model model) {

        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        User user = userDAO.findByUsername(username);// logged in passenger
        System.out.print("userId: "+user.getUserId()+ " username: "+user.getUsername());
        System.out.println("Concern: "+concern.getDescription());
        System.out.println();
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user not found
        }

        concern.setPassengerId(user.getUserId());
        concern.setStatus("Pending");
        concern.setDate_submitted(LocalDateTime.now());
        try{
            concernDAO.save(concern);
            model.addAttribute("message", "Concern submitted successfully!");

        }
        catch(Exception e){
            model.addAttribute("errorMessage", "Concern submission failed!");
            System.out.println(e.getMessage());
        }
        return "passenger/concern_page"; // maps to concern_page.html in templates
    }
}
