package org.example.trainsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.example.trainsystem.entity.*;
import org.example.trainsystem.repository.*;
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

    @Autowired
    private DriverDAO driverDAO;

    @Autowired
    private OpManagerDAO opManagerDAO;

    @Autowired
    private TicketOfficerDAO ticketOfficerDAO;

    ITOfficer itOfficer = new ITOfficer();

    @Autowired
    private CustomerConcernExecutiveDAO customerConcernExecutiveDAO;



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

    @GetMapping("driver/register")
    public String showDriverForm(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        return "driver/driver";
    }

    @PostMapping("driver/register")
    public String saveDriver(@RequestParam("userId") int userId,
                                @RequestParam("license") String license) {

        Driver driver = new Driver();
        driver.setUserId(userId);
        driver.setLicense(license);
        driver.setTrainId(1); // Default train assignment
        driverDAO.save(driver);
        return "redirect:/it/dashboard";
    }

    @GetMapping("opmanager/register")
    public String showOpManagerForm(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        return "opmanager/opmanager";
    }

    @PostMapping("opmanager/register")
    public String saveOpManager(@RequestParam("userId") int userId,
                                @RequestParam("contactNumber") String contactNumber) {

        OpManager opManager = new OpManager();
        opManager.setUserId(userId);
        opManager.setContactNumber(contactNumber);
        opManagerDAO.save(opManager);

        return  "redirect:/it/dashboard";
    }

    @GetMapping("concern/register")
    public String showConcernManagerForm(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        return "concern/concernExecutive";
    }

    @PostMapping("concern/register")
    public String saveConcernManager(@RequestParam("userId") int userId,
                                     @RequestParam("contactNumber") String contactNumber){
        CustomerServiceExecutive customerServiceExecutive = new CustomerServiceExecutive();
        customerServiceExecutive.setUserId(userId);
        customerServiceExecutive.setContactNum(contactNumber);

        customerConcernExecutiveDAO.save(customerServiceExecutive);
        return  "redirect:/it/dashboard";
    }

    @GetMapping("ticketOfficer/register")
    public String showTicketOfficerForm(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("userId", userId);
        TicketOfficer ticketOfficer = new TicketOfficer();
        ticketOfficer.setTrainId(1);
        ticketOfficer.setUserId(userId);
        ticketOfficer.setTrainId(1); // Default train assignment
        ticketOfficerDAO.save(ticketOfficer);


        return "redirect:/it/dashboard";
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
