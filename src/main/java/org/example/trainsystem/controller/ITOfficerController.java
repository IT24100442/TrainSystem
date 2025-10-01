package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.ITOfficer;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.repository.ITOfficerDAO;
import org.example.trainsystem.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/it")
public class ITOfficerController {

    @Autowired
    private ITOfficerDAO itOfficerDAO;

    @Autowired
    private UserDAO userDAO;

    ITOfficer itOfficer = new ITOfficer();



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

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        List<User> users = userDAO.findAll();

        model.addAttribute("users", users);

        return "it/dashboard"; // Thymeleaf template
    }

    @GetMapping("/register")
    public String showItOfficerForm(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        return "it/it-officer";
    }

    @PostMapping("/register")
    public String saveItOfficer(@RequestParam("userId") int userId,
                                @RequestParam("accessLevel") String accessLevel) {

        itOfficer.setUserId(userId);
        itOfficer.setAccessLevel(accessLevel);
        itOfficerDAO.save(itOfficer);
        return "redirect:/login";
    }

    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        userDAO.delete(userId);
        if(userId == itOfficer.getUserId()){
            SecurityContextHolder.clearContext();
            return "redirect:/login?logout";
        }
        return "redirect:/it/dashboard";
    }

}
