package org.example.trainsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.trainsystem.repository.*;
import org.example.trainsystem.entity.*;
import java.util.List;
import java.util.Collections;

@Service
public class PassengerService {

    @Autowired
    private PassengerDAO passengerDAO;

    // Get passenger with user details (expects DAO to return joined Passenger+User)
    public Passenger getPassengerWithUser(String username) {
        Passenger passenger = passengerDAO.findPassengerWithUser(username);
        if (passenger == null) {
            throw new RuntimeException("Passenger not found with username: " + username);
        }
        // optional sanity-check: ensure discriminator
        if (passenger.getUserType() != null && !"passenger".equalsIgnoreCase(passenger.getUserType())) {
            throw new RuntimeException("User found is not a passenger: userType=" + passenger.getUserType());
        }
        return passenger;
    }

    // Get passenger by ID
    public Passenger getPassengerById(int userId) {
        Passenger passenger = passengerDAO.findPassengerById(String.valueOf(userId));
        if (passenger == null) {
            throw new RuntimeException("Passenger not found with userId: " + userId);
        }
        return passenger;
    }

    // Get all passengers (DAO must return fully populated Passenger objects)
    public List<Passenger> getAllPassengers() {
        List<Passenger> passengers = passengerDAO.findAllPassengers();
        return passengers == null ? Collections.emptyList() : passengers;
    }

    // Search passengers by address (passenger-specific method)
    public List<Passenger> searchPassengersByAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new RuntimeException("Search address cannot be empty");
        }
        return passengerDAO.findByAddressContaining(address.trim());
    }

    // Update passenger details (address)
    public void updatePassengerDetails(String userId, String address) {
        Passenger passenger = passengerDAO.findPassengerById(userId);
        if (passenger == null) {
            throw new RuntimeException("Passenger not found with userId: " + userId);
        }

        passenger.setAddress(address);
        int result = passengerDAO.update(passenger);
        if (result == 0) {
            throw new RuntimeException("Failed to update passenger details");
        }
    }

    // Create new passenger
    // NOTE: This method assumes passengerDAO.save(...) will insert both user (users table)
    // and passenger (passengers table) rows as needed. If your DAO only writes the passengers
    // table, you must insert a user row first (via userDAO).
    public void createPassenger(String userId, String address) {
        Passenger existing = passengerDAO.findPassengerById(userId);
        if (existing != null) {
            throw new RuntimeException("Passenger already exists with userId: " + userId);
        }

        Passenger passenger = new Passenger();
        passenger.setUserId(Integer.parseInt(userId));
        passenger.setUserType("passenger"); // discriminator for ISA
        passenger.setAddress(address);

        int result = passengerDAO.save(passenger);
        if (result == 0) {
            throw new RuntimeException("Failed to create passenger");
        }
    }

    // Delete passenger
    public void deletePassenger(String userId) {
        Passenger passenger = passengerDAO.findPassengerById(userId);
        if (passenger == null) {
            throw new RuntimeException("Passenger not found with userId: " + userId);
        }

        int result = passengerDAO.delete(userId);
        if (result == 0) {
            throw new RuntimeException("Failed to delete passenger");
        }
    }

    // Get passenger count
    public int getTotalPassengerCount() {
        return passengerDAO.countAllPassengers();
    }

    // Check existence
    public boolean passengerExists(String userId) {
        Passenger passenger = passengerDAO.findPassengerById(userId);
        return passenger != null;
    }
}
