package org.example.trainsystem.controller;


import org.example.trainsystem.entity.*;
import org.example.trainsystem.repository.*;
import org.example.trainsystem.service.RouteService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/trains")
public class TrainController {

    @Autowired
    TrainDAO trainDAO;

    @Autowired
    TrainRouteDAO trainRouteDAO;

    @Autowired
    RouteService routeService;

    @Autowired
    OpManagerDAO opManagerDAO;

    @Autowired
    UserDAO userDAO;


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



    @PostMapping("/add")
    public String addTrain(@RequestParam("trainName") String trainName, Model model, RedirectAttributes redirectAttributes) {

//        String username = getAuthenticatedUsername();
//        if (username == null) {
//            return "redirect:/login";
//        }
//
//        User user = userDAO.findByUsername(username);// logged in passenger
//        if (user == null) {
//            return "redirect:/login"; // Redirect to login if user not found
//        }
//
//        OpManager opManager = user.getOpManager();
//

        Train train = new Train();
        train.setName(trainName);

        int trainId = 0;

        try{
            trainDAO.save(train);
            trainId = trainDAO.getLastTrainId();
            System.out.println("Train added successfully: " + trainId);
            model.addAttribute("successMessage", "Train added successfully");
            return "redirect:/trains/addTrainRoute?trainId="+trainId;
        }
        catch (Exception e){
            redirectAttributes.addAttribute("errorMessage", "Train already exists");
            return "redirect:/opmanager/dashboard";
        }


    }

    @GetMapping("/view")
    public String viewTrain(Model model) {
        model.addAttribute("trains", trainDAO.getAllTrains());
        return "redirect: /opmanager/view_trains";
    }

    @GetMapping("/addTrainRoute")
    public String showAddTrainRouteTrainForm(@RequestParam("trainId") Integer trainId, Model model) {

        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);

        model.addAttribute("trainId", trainId);
        return "opmanager/addTrainRoute";
    }

    @PostMapping("/addTrainRoute")
    public String addTrainRoute(@RequestParam("trainId") Integer trainId,
                                @RequestParam("routeId") Integer routeId,
                                Model model) {
        Train train = trainDAO.getTrainById(trainId);
        if (train == null) {
            model.addAttribute("error", "Train not found");
            return "trains/addTrainRoute";
        }

        TrainRoute trainRoute = new TrainRoute();
        trainRoute.setTrainId(trainId);
        trainRoute.setRouteId(routeId);


        try{
            trainRouteDAO.save(trainRoute);
            model.addAttribute("success", "Train route assigned successfully");
            return "redirect:/train-status/addStatus?trainRouteId="+trainRouteDAO.getLastInsertId();

        }
        catch (Exception e){
            model.addAttribute("error", "Error saving train route. It might already exist.");
            return "redirect:/trains/addTrainRoute?trainId="+trainId;
        }




    }




}
