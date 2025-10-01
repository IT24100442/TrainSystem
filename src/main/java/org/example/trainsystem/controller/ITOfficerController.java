package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.ITOfficer;
import org.example.trainsystem.repository.ITOfficerDAO;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/it")
public class ITOfficerController {

    @Autowired
    private ITOfficerDAO itOfficerDAO;

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

}
