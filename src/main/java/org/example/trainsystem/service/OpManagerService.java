package org.example.trainsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.trainsystem.repository.*;
import org.example.trainsystem.entity.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OpManagerService {

    @Autowired
    private UserDAO userDAO;          // For operation managers

    @Autowired
    private DriverDAO driverDAO;      // To fetch drivers

    @Autowired
    private MessageDAO messageDAO;    // To send messages

    /**
     * Get operation manager info by username
     */
    public User getOpManagerByUsername(String username) {
        User opManager = userDAO.findByUsername(username);
        if (opManager == null) {
            throw new RuntimeException("Operation Manager not found with username: " + username);
        }
        return opManager;
    }

    /**
     * Get all drivers
     */
    public List<Driver> getAllDrivers() {
        return driverDAO.findAllDrivers();
    }

    /**
     * Get driver by ID
     */
    public Driver getDriverById(int driverId) {
        Driver driver = driverDAO.findDriverById(driverId);
        if (driver == null) {
            throw new RuntimeException("Driver not found with id: " + driverId);
        }
        return driver;
    }

    /**
     * Send message from operation manager to driver
     */
    public void sendMessage(int opManagerId, int driverId, String messageText) {
        Message message = new Message(opManagerId, driverId, messageText, LocalDateTime.now());
        int result = messageDAO.save(message);
        if (result == 0) {
            throw new RuntimeException("Failed to send message");
        }
    }

    /**
     * Fetch recent messages for a driver
     */
    public List<Message> getDriverMessages(int driverId, int limit) {
        return messageDAO.findRecentMessagesByReceiverId(driverId, limit);
    }
}
