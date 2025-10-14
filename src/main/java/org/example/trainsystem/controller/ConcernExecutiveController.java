package org.example.trainsystem.controller;

import org.example.trainsystem.entity.CustomerServiceExecutive;
import org.example.trainsystem.entity.Driver;
import org.example.trainsystem.entity.Reply;
import org.example.trainsystem.repository.ConcernDAO;
import org.example.trainsystem.repository.CustomerConcernExecutiveDAO;
import org.example.trainsystem.repository.ReplyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/concernExecutive")
public class ConcernExecutiveController {
    @Autowired
    private CustomerConcernExecutiveDAO customerServiceExecutiveDAO;

    @Autowired
    private ConcernDAO concernDAO;

    @Autowired
    private ReplyDAO replyDAO;


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
    public String showDashboard(Model model, String message, String errorMessage) {

        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        // Fetch executive info using username
        CustomerServiceExecutive customerServiceExecutive = customerServiceExecutiveDAO.findCustomerExecutiveWithUser(username);
        model.addAttribute("executive", customerServiceExecutive);

        model.addAttribute("concerns", concernDAO.findAll());
        model.addAttribute("Message", message);
        model.addAttribute("ErrorMessage", errorMessage);


//        model.addAttribute("reply", new Reply());


        return "concern/dashboard";

    }

    @PostMapping("/reply")
    public String submitReply(String replyMessage, int concernId, RedirectAttributes redirectAttributes) {

        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        // Fetch executive info using username
        CustomerServiceExecutive customerServiceExecutive = customerServiceExecutiveDAO.findCustomerExecutiveWithUser(username);

        Reply reply = new Reply();
        reply.setConcernId(concernId);
        reply.setExecId(customerServiceExecutive.getUserId());
        reply.setReplyText(replyMessage);
        reply.setReplyTime(LocalDateTime.now());


        try{
            replyDAO.save(reply);
            redirectAttributes.addFlashAttribute("message", "Reply submitted successfully.");
        }
        catch (Exception e){
            System.out.println("Error saving reply: "+e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to submit reply. Please try again.");
        }


        return "redirect:/concernExecutive/dashboard";
    }
}
