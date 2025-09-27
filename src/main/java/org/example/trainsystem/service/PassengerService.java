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
        Passenger passenger = passengerDAO.findPassengerById(Integer.parseInt(String.valueOf(userId)));
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

    // Save passenger
    public int save(Passenger passenger) {
        return passengerDAO.save(passenger);  // Uses JPA repository to persist
    }

    // Update passenger details (address)
    public void updatePassengerDetails(int userId, String address) {
        // Find passenger by int userId directly
        Passenger passenger = passengerDAO.findPassengerById(userId);  // Use the updated DAO method
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
    // Create new passenger
    public void createPassenger(Passenger passenger) {
        if (passenger == null) {
            throw new RuntimeException("Passenger cannot be null");
        }

        Passenger existing = passengerDAO.findPassengerById(passenger.getUserId());
        if (existing != null) {
            throw new RuntimeException("Passenger already exists with userId: " + passenger.getUserId());
        }

        String passengerCode = "P" + (passengerDAO.countAllPassengers() + 1);
        passenger.setPassengerCode(passengerCode);

        // Make sure userType is set correctly
        passenger.setUserType("PASSENGER");

        // Default address if null
        if (passenger.getAddress() == null) {
            passenger.setAddress("N/A");
        }

        int result = passengerDAO.save(passenger);
        if (result == 0) {
            throw new RuntimeException("Failed to create passenger");
        }
    }


    // Delete passenger
    public void deletePassenger(String userId) {
        Passenger passenger = passengerDAO.findPassengerById(Integer.parseInt(userId));
        if (passenger == null) {
            throw new RuntimeException("Passenger not found with userId: " + userId);
        }

        int result = passengerDAO.delete(Integer.parseInt(userId));
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
        Passenger passenger = passengerDAO.findPassengerById(Integer.parseInt(userId));
        return passenger != null;
    }
}
