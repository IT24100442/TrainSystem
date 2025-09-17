package org.example.trainsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.trainsystem.repository.*;
import org.example.trainsystem.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverDAO driverDAO;

    @Autowired
    private RouteDAO routeDAO;

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private StatusUpdateDAO statusUpdateDAO;

    public Driver getDriverWithUser(String username) {
        Driver driver = driverDAO.findDriverWithUser(username);
        if (driver == null) {
            throw new RuntimeException("Driver not found with username: " + username);
        }
        return driver;
    }

    public Route getDriverRoute(String driverId) {
        return routeDAO.findByDriverId(driverId);
    }

    public List<Message> getRecentMessages(String driverId, int limit) {
        return messageDAO.findRecentMessagesByReceiverId(driverId, limit);
    }

    public StatusUpdate getLatestLocation(String trainId) {
        return StatusUpdateDAO.findLatestByTrainId(trainId);
    }

    public void updateLocation(String trainId, String location, String status, String remarks) {
        StatusUpdate statusUpdate = new StatusUpdate(trainId, location, status, remarks, LocalDateTime.now());
        int result = StatusUpdateDAO.save(statusUpdate);
        if (result == 0) {
            throw new RuntimeException("Failed to update statusUpdate");
        }
    }

    public void sendMessage(String senderId, String receiverId, String messageText) {
        Message message = new Message(senderId, receiverId, messageText, LocalDateTime.now());
        int result = messageDAO.save(message);
        if (result == 0) {
            throw new RuntimeException("Failed to send message");
        }
    }

    public List<StatusUpdate> getLocationHistory(String trainId) {
        return statusUpdateDAO.findByTrainId(trainId);
    }
}