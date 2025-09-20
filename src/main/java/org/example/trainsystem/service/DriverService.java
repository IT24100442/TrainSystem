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

    @Autowired
    private UserDAO userDAO;

    public Driver getDriverWithUser(String username) {
        Driver driver = driverDAO.findDriverWithUser(username);
        if (driver == null) {
            throw new RuntimeException("Driver not found with username: " + username);
        }
        return driver;
    }

    public Driver getDriverById(int userId) {
        return driverDAO.findDriverById(userId);
    }

    public Route getDriverRoute(int driverId) {
        return routeDAO.findByDriverId(driverId);
    }

    public List<Message> getRecentMessages(int driverId, int limit) {
        return messageDAO.findRecentMessagesByReceiverId(driverId, limit);
    }

    public StatusUpdate getLatestLocation(int trainId) {
        return StatusUpdateDAO.findLatestByTrainId(trainId);
    }

    public void updateLocation(int trainId, String location, String status) {
        StatusUpdate statusUpdate = new StatusUpdate(trainId, location, status, LocalDateTime.now());
        int result = StatusUpdateDAO.save(statusUpdate);
        if (result == 0) {
            throw new RuntimeException("Failed to update statusUpdate");
        }
    }
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = driverDAO.findAllDrivers();
        for (Driver d : drivers) {
            User user = userDAO.findById(d.getUserId());
            d.setUser(user); // Attach the User to the Driver
        }
        return drivers;
    }

    public void sendMessage(int senderId, int receiverId, String messageText) {
        Message message = new Message(senderId, receiverId, messageText, LocalDateTime.now());
        int result = messageDAO.save(message);
        if (result == 0) {
            throw new RuntimeException("Failed to send message");
        }
    }

    public List<StatusUpdate> getLocationHistory(int trainId) {
        return statusUpdateDAO.findByTrainId(trainId);
    }
}