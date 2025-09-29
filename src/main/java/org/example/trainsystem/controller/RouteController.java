package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Route;
import org.example.trainsystem.entity.Stop;
import org.example.trainsystem.repository.RouteDAO;
import org.example.trainsystem.repository.StopDAO;
import org.example.trainsystem.service.DriverService;
import org.example.trainsystem.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private RouteDAO routeDAO;

    @Autowired
    private StopDAO stopsDAO;

    @Autowired
    private DriverService driverService;

    // ✅ List all routes with stops and driver info
    @GetMapping("/list")
    public String listRoutes(Model model) {
        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        return "opmanager/listRoutes";
    }

    // ✅ Show form to add a new route
    @GetMapping("/add")
    public String showAddRouteForm(Model model) {
        Route route = new Route();
        // Initialize 5 stops for the form
        for (int i = 0; i < 10; i++) {
            route.getStops().add(new Stop());
        }
        model.addAttribute("route", route);
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "opmanager/addRouteWithStops"; // create this Thymeleaf page
    }

    // ✅ Save route and stops to DB
    @PostMapping("/add")
    public String addRouteWithStops(@ModelAttribute Route route) {
        // 1️⃣ Save route
        routeDAO.save(route);

        // 2️⃣ Get generated routeId
        Route savedRoute = routeDAO.findByName(route.getRouteName());
        if (savedRoute == null) return "redirect:/routes/list"; // safety check
        int routeId = savedRoute.getRouteId();

        // 3️⃣ Save stops
        int order = 1;
        for (Stop stop : route.getStops()) {
            if (stop.getStopName() != null && !stop.getStopName().isEmpty()) {
                stop.setRouteId(routeId);
                stop.setStopOrder(order++);
                stopsDAO.save(stop);
            }
        }

        return "redirect:/routes/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoute(@PathVariable("id") int id) {
        routeService.deleteRoute(id); // call service/DAO to delete
        return "redirect:/routes/list";    // redirect back to the routes list
    }
}
