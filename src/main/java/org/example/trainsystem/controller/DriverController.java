package org.example.trainsystem.controller;

import org.example.trainsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.trainsystem.dto.StatusUpdateRequest;
import org.example.trainsystem.dto.MessageRequest;
import org.example.trainsystem.entity.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private UserService userService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TrainStatusService trainStatusService;

    @Autowired
    StatusUpdateService statusUpdateService;

    // Utility method to get the authenticated username
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
    public String driverDashboard(Model model) {
        String username = getAuthenticatedUsername();
        if (username == null) {
            return "redirect:/login";
        }

        // Fetch driver info using username
        Driver driver = driverService.getDriverWithUser(username);
        model.addAttribute("driver", driver);

        Route route = routeService.getRouteByDriverId(driver.getUserId());

        if(route == null){
            System.out.println("Route not found");
        }
        model.addAttribute("route", route);

        List<Message> messages = driverService.getRecentMessages(driver.getUserId(), 10);
        model.addAttribute("messages", messages);

        StatusUpdate latestLocation = driverService.getLatestLocation(driver.getUserId());
        model.addAttribute("currentLocation", latestLocation);

        List<User> operationsManagers = userService.getOperationsManagers();
        model.addAttribute("operationsManagers", operationsManagers);

        return "driver/dashboard";
    }

    @PostMapping("/update-location")
    public String updateLocation(@RequestParam int trainRouteId, @RequestParam String stopName) {
        trainStatusService.updateDriverLocation(trainRouteId, stopName);
        return "redirect:/driver/dashboard";
    }

    @PostMapping("/next-stop")
    @ResponseBody
    public Map<String, Object> updateNextStop() {
        Map<String, Object> response = new HashMap<>();
        String username = getAuthenticatedUsername();
        if (username == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return response;
        }

        Driver driver = driverService.getDriverWithUser(username);
        Route route = routeService.getRouteByDriverId(driver.getUserId());

        List<Stop> stops = route.getStops();
        String currentLocation = driverService.getLatestLocation(driver.getUserId()).getCurrentLocation();
        String nextStop = null;

        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getStopName().equalsIgnoreCase(currentLocation) && i + 1 < stops.size()) {
                nextStop = stops.get(i + 1).getStopName();
                break;
            }
        }

        // If current stop is last, stay there
        if (nextStop == null && !stops.isEmpty()) {
            nextStop = stops.get(stops.size() - 1).getStopName();
        }

        // Save location update
        statusUpdateService.save(new StatusUpdate(driver.getTrainId(), nextStop, "On Schedule"));

        response.put("success", true);
        response.put("nextStop", nextStop);
        return response;
    }



    @PostMapping("/send-message")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody MessageRequest request) {
        String username = getAuthenticatedUsername();
        Map<String, Object> response = new HashMap<>();

        if (username == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Driver driver = driverService.getDriverWithUser(username);
            driverService.sendMessage(driver.getUserId(), request.getReceiverId(), request.getMessageText());
            response.put("success", true);
            response.put("message", "Message sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error sending message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/messages/refresh")
    @ResponseBody
    public ResponseEntity<List<Message>> refreshMessages() {
        String username = getAuthenticatedUsername();
        if (username == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            Driver driver = driverService.getDriverWithUser(username);
            List<Message> messages = driverService.getRecentMessages((driver.getUserId()), 10);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/location-history")
    @ResponseBody
    public ResponseEntity<List<StatusUpdate>> getLocationHistory() {
        String username = getAuthenticatedUsername();
        if (username == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            Driver driver = driverService.getDriverWithUser(username);
            List<StatusUpdate> locationHistory = driverService.getLocationHistory(driver.getUserId());
            return ResponseEntity.ok(locationHistory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
