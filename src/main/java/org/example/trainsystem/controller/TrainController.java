package org.example.trainsystem.controller;


import org.example.trainsystem.entity.Route;
import org.example.trainsystem.entity.Train;
import org.example.trainsystem.entity.TrainRoute;
import org.example.trainsystem.entity.TrainStatus;
import org.example.trainsystem.repository.TrainDAO;
import org.example.trainsystem.repository.TrainRouteDAO;
import org.example.trainsystem.repository.TrainStatusDAO;
import org.example.trainsystem.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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




    @PostMapping("/add")
    public String addTrain(@RequestParam("trainName") String trainName, Model model) {
        Train train = new Train();
        train.setName(trainName);

        trainDAO.save(train);

        int trainId = trainDAO.getLastTrainId();
        // Send success message back to the view
        System.out.println("Train added successfully: " + trainId);


        return "redirect:/trains/addTrainRoute?trainId="+trainId;
    }

    @GetMapping("/view")
    public String viewTrain(Model model) {
        model.addAttribute("trains", trainDAO.getAllTrains());
        return "opmanager/view_trains";
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

        trainRouteDAO.save(trainRoute);



        model.addAttribute("success", "Train route assigned successfully");
        return "redirect:/train-status/addStatus?trainRouteId="+trainRouteDAO.getLastInsertId();
    }




}
