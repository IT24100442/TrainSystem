package org.example.trainsystem.controller;

import org.example.trainsystem.entity.TrainStatus;
import org.example.trainsystem.repository.RouteDAO;
import org.example.trainsystem.repository.StopDAO;
import org.example.trainsystem.repository.TrainStatusDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/train-status")
public class TrainStatusController {

    @Autowired
    private TrainStatusDAO statusrepo;

    @Autowired
    private RouteDAO routeRepo; // Assuming you have one
    @Autowired
    private StopDAO stopRepo;   // Assuming you have one

    @GetMapping("/override")
    public String showOverrideForm(Model model) {
        model.addAttribute("trainStatus", new TrainStatus());
        model.addAttribute("routes", routeRepo.findAll());
        model.addAttribute("stops", stopRepo.findAll());
        model.addAttribute("statuses", statusrepo.findAllWithDetails()); // Show all statuses
        return "opmanager/override-status";
    }

    @PostMapping("/override")
    public String overrideStatus(@ModelAttribute TrainStatus trainStatus) {
        trainStatus.setTimestamp(LocalDateTime.now());
        statusrepo.save(trainStatus);
        return "redirect:/train-status/override";
    }
}
