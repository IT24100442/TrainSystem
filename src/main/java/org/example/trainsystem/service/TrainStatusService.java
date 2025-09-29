package org.example.trainsystem.service;

import org.example.trainsystem.entity.Stop;
import org.example.trainsystem.entity.TrainRoute;
import org.example.trainsystem.entity.TrainStatus;
import org.example.trainsystem.repository.StopDAO;
import org.example.trainsystem.repository.TrainRouteDAO;
import org.example.trainsystem.repository.TrainStatusDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrainStatusService {

    @Autowired
    private TrainRouteDAO trainRouteDAO;

    @Autowired
    private TrainStatusDAO trainStatusDAO;

    @Autowired
    private StopDAO stopDAO;

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

    public String getNextStop(Integer routeId, String currentStop) {
        List<Stop> stops = stopDAO.findByRouteId(routeId);

        int idx = -1;
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getStopName().equalsIgnoreCase(currentStop)) {
                idx = i;
                break;
            }
        }

        if (idx == -1 || idx == stops.size() - 1) {
            return null; // no next stop
        }

        return stops.get(idx + 1).getStopName();
    }

    /**
     * Updates the train's current stop in TrainStatus table.
     */
    public void updateStop(Integer routeId, int trainId, String nextStop, LocalDateTime time) {
        if (nextStop == null) {
            throw new IllegalStateException("No next stop available for this route.");
        }

        // 1. Resolve trainRoute from trainId
        TrainRoute trainRoute = trainRouteDAO.findByTrainId(trainId);
        System.out.println("routeId: " + routeId + ", trainId: " + trainId + " trainRoute: "+ trainRoute) ;
        if (trainRoute == null) {
            throw new IllegalArgumentException("No TrainRoute found for routeId: " + routeId);
        }

        // 2. Get stopId for the stop name
        Optional<Stop> stopOpt = Optional.ofNullable(stopDAO.findByName(nextStop));
        if (stopOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid stop: " + nextStop);
        }
        int stopId = stopOpt.get().getId();

        // 3. Build and save TrainStatus
        int statusId = trainStatusDAO.find1ByTrainRoute(trainRoute.getTrainRouteId());

        TrainStatus status = new TrainStatus();
        status.setTrainRouteId(trainRoute.getTrainRouteId()); // <-- correct ID now
        status.setStopId(stopId);
        status.setTimestamp(time);

        trainStatusDAO.updateStop(statusId, status.getStopId());
    }
}
