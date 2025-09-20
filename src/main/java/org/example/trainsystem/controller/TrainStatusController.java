package org.example.trainsystem.controller;

import org.example.trainsystem.entity.Route;
import org.example.trainsystem.entity.TrainRoute;
import org.example.trainsystem.entity.TrainStatus;
import org.example.trainsystem.repository.RouteDAO;
import org.example.trainsystem.repository.StopDAO;
import org.example.trainsystem.repository.TrainRouteDAO;
import org.example.trainsystem.repository.TrainStatusDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/train-status")
public class TrainStatusController {

    @Autowired
    private TrainStatusDAO statusrepo;

    @Autowired
    private RouteDAO routeRepo;

    @Autowired
    private StopDAO stopRepo;

    @Autowired
    private TrainRouteDAO trainRouteDAO;

    // Java
    @GetMapping("/override")
    public String showOverrideForm( Model model) {
        model.addAttribute("routes", routeRepo.findAll());
        model.addAttribute("stops", stopRepo.findAll());
        model.addAttribute("trainRoutes", trainRouteDAO.findAll());
        List<TrainStatus> statuses = statusrepo.findAllWithDetails();

        // Java
        for (TrainStatus status : statuses) {
            System.out.println("TrainStatus ID: " + status.getStatusId() + ", TrainRouteId: " + status.getTrainRouteId());
            TrainRoute trainRoute = trainRouteDAO.findById(status.getTrainRouteId());
            if (trainRoute != null) {
                System.out.println("Found TrainRoute: " + trainRoute.getTrainRouteId() + ", RouteId: " + trainRoute.getRouteId());
                Route route = routeRepo.findById(trainRoute.getRouteId());
                if (route != null) {
                    status.setRouteName(route.getRouteName());
                } else {
                    System.out.println("Route not found for RouteId: " + trainRoute.getRouteId());
                    status.setRouteName("Unknown");
                }
            } else {
                System.out.println("TrainRoute not found for TrainRouteId: " + status.getTrainRouteId());
                status.setRouteName("Unknown");
            }
        }

        model.addAttribute("statuses", statuses);
        return "opmanager/override-status";
    }

    @PostMapping("/override")
    public String overrideStatus(@ModelAttribute TrainStatus trainStatus) {
        trainStatus.setTimestamp(LocalDateTime.now());
        System.out.println("Status: " + trainStatus.getStatus());
        System.out.println("StatusId: " + trainStatus.getStatusId());

        if (trainStatus != null && trainStatus.getStatusId() > 0) {
            statusrepo.updateStatus(trainStatus.getStatusId(), trainStatus.getStatus());
        } else {
            statusrepo.save(trainStatus);
        }

        return "redirect:/train-status/override";
    }
}