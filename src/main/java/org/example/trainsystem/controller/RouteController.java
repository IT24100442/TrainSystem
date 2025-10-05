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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    //  List all routes with stops and driver info
    @GetMapping("/list")
    public String listRoutes(Model model) {
        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        return "opmanager/listRoutes";
    }

    // Show form to add a new route
    @GetMapping("/add")
    public String showAddRouteForm(Model model) {
        Route route = new Route();
        // Initialize 10 stops for the form
        for (int i = 0; i < 10; i++) {
            route.getStops().add(new Stop());
        }
        model.addAttribute("route", route);
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "opmanager/addRouteWithStops"; // create this Thymeleaf page
    }

    //  Save route and stops to DB
    @PostMapping("/add")
    public String addRouteWithStops(@ModelAttribute Route route,  RedirectAttributes redirectAttributes) {


        try{
            routeDAO.save(route);
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving route: " + e.getMessage());
            System.out.println("Error saving route: " + e.getMessage());
            return "redirect:/routes/add";
        }



        //  Get generated routeId
        Route savedRoute = routeDAO.findByName(route.getRouteName());
        if (savedRoute == null) return "redirect:/routes/list"; // safety check
        int routeId = savedRoute.getRouteId();

        //Save stops
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

    @GetMapping("/edit/{id}")
    public String showEditRouteForm(@PathVariable("id") int id, Model model) {
        Route newRoute = new Route();
        newRoute.setRouteId(id);
        // Initialize 10 stops for the form
        for (int i = 0; i < 10; i++) {
            newRoute.getStops().add(new Stop());
        }
        model.addAttribute("newRoute", newRoute);
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "opmanager/editRouteWithStops"; // create this Thymeleaf page

    }

    @PostMapping("/edit")
    public String editRouteWithStops(@ModelAttribute Route newRoute, RedirectAttributes redirectAttributes) {
        Route existingRoute = routeDAO.findById(newRoute.getRouteId());
        System.out.println("Editing route: "+newRoute.getRouteId()+" name: "+newRoute.getRouteName());
        if (existingRoute == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Route not found");
            return "redirect:/routes/edit/"+newRoute.getRouteId();
        }
        try{
            try{
                stopsDAO.deleteByRouteId(newRoute.getRouteId());

            }
            catch (Exception e) {
                System.out.println("Error deleting existing stops: "+e.getMessage());
                return "redirect:/routes/edit/"+newRoute.getRouteId();

            }


            routeDAO.update(newRoute);
            int order = 1;
            for (Stop stop : newRoute.getStops()) {
                if (stop.getStopName() != null && !stop.getStopName().isEmpty()) {
                    stop.setRouteId(newRoute.getRouteId());
                    stop.setStopOrder(order++);
                    stopsDAO.save(stop);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error updating route: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating route: " + e.getMessage());
            return "redirect:/routes/edit/"+newRoute.getRouteId();
        }




       return "redirect:/routes/list";


    }




}
