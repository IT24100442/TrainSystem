package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Driver;
import org.example.trainsystem.entity.Message;
import org.example.trainsystem.entity.User;
import org.example.trainsystem.service.DriverService;
import org.example.trainsystem.service.MessageService;
import org.example.trainsystem.service.OpManagerService;
import org.example.trainsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/opmanager")
public class OpManagerController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    // Utility to get logged-in username
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

    // Dashboard page
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = getAuthenticatedUsername();
        if (username == null) return "redirect:/login";

        // Fetch operation manager info
        User opManager = userService.getUserByUsername(username);
        model.addAttribute("opManager", opManager);

        // Fetch all drivers
        List<Driver> drivers = driverService.getAllDrivers();
        model.addAttribute("drivers", drivers);

        // Fetch recent messages for first driver as example
        List<Message> driverMessages = new ArrayList<>();
        for (Driver driver : drivers) {
            List<Message> msgs = messageService.getMessagesSentforManager(opManager.getUserId());
            for (Message msg : msgs) {
                msg.setDriverName(driver.getUser().getName());
            }
            driverMessages.addAll(msgs);
        }


        model.addAttribute("driverMessages", driverMessages);

        return "opmanager/dashboard";
    }

    // View driver route and status
    @GetMapping("/driver/{driverId}")
    public String viewDriver(@PathVariable int driverId, Model model) {
        Driver driver = driverService.getDriverById(driverId);
        if (driver == null) return "redirect:/opmanager/dashboard";

        model.addAttribute("driver", driver);

        // Fetch route
        model.addAttribute("route", driverService.getDriverRoute(driver.getUserId()));

        // Fetch latest location
        model.addAttribute("currentLocation", driverService.getLatestLocation(driver.getUserId()));

        // Fetch recent messages
        model.addAttribute("messages", driverService.getRecentMessages(driver.getUserId(), 10));

        return "opmanager/view-driver";
    }

    // Send message to a driver
    @PostMapping("/send-message")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> request) {
        String username = getAuthenticatedUsername();
        Map<String, Object> response = new HashMap<>();

        if (username == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            int driverId = Integer.parseInt(request.get("driverId"));
            String messageText = request.get("messageText");

            User opManager = userService.getUserByUsername(username);
            int senderId = opManager.getUserId();

            driverService.sendMessage(senderId, driverId, messageText);

            response.put("success", true);
            response.put("message", "Message sent successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error sending message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
