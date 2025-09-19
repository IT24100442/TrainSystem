package org.example.trainsystem.service;

import org.example.trainsystem.entity.TrainStatus;
import org.example.trainsystem.repository.TrainStatusDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStatusService {

    @Autowired
    private TrainStatusDAO trainStatusDAO;

    public void updateDriverLocation(int trainRouteId, String stopName) {
        TrainStatus status = new TrainStatus();
        status.setTrainRouteId(trainRouteId);
        status.setStopName(stopName);
        status.setStatus("ON_TIME");
        trainStatusDAO.save(status);
    }

    public void overrideStatus(int statusId, String newStatus) {
        trainStatusDAO.updateStatus(statusId, newStatus);
    }

    public List<TrainStatus> getStatusesForRoute(int trainRouteId) {
        return trainStatusDAO.findByTrainRoute(trainRouteId);
    }
}
