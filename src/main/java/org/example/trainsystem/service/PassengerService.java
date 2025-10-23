package org.example.trainsystem.service;

import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.repository.PassengerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PassengerService {

    @Autowired
    private PassengerDAO passengerDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /// register new passenger
    public void registerPassenger(org.example.trainsystem.dto.UserDTO userDTO) {
        // Check if username or email already exists
        if (findByUsername(userDTO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // Create a new Passenger entity
        Passenger passenger = new Passenger();
        passenger.setName(userDTO.getName());
        passenger.setUsername(userDTO.getUsername());
        passenger.setEmail(userDTO.getEmail());
        passenger.setAddress(userDTO.getAddress());
        passenger.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Save to database
        passengerDAO.save(passenger);
    }


    /// Find passenger by username
    public Passenger findByUsername(String username) {
        return passengerDAO.findByUsername(username);
    }


    /// Find passenger by email
    public Passenger findByEmail(String email) {
        return passengerDAO.findByEmail(email);
    }

    /**
     * Update passenger information
     * Users fields: name, email
     * Passenger field: address
     */
    public void updatePassenger(String username, String name, String email,
                                String address) {
        Passenger passenger = findByUsername(username);

        if (passenger == null) {
            throw new RuntimeException("Passenger not found");
        }

        // Check if email is already taken by another user
        Passenger existingWithEmail = findByEmail(email);
        if (existingWithEmail != null && existingWithEmail.getUserId() != passenger.getUserId()) {
            throw new RuntimeException("Email already in use");
        }

        // Update both User fields (name, email) and Passenger fields (address) in one call
        passengerDAO.updatePassengerWithUser(passenger.getUserId(), name, email, address);
    }


    /// Change passenger password
    public void changePassword(String username, String currentPassword, String newPassword) {
        Passenger passenger = findByUsername(username);

        if (passenger == null) {
            throw new RuntimeException("Passenger not found");
        }

        // Verify current password (using inherited getter)
        if (!passwordEncoder.matches(currentPassword, passenger.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Validate new password strength
        if (newPassword.length() < 5) {
            throw new RuntimeException("New password must be at least 8 characters long");
        }

        // Update password in Users table
        passengerDAO.updatePassword(passenger.getUserId(), passwordEncoder.encode(newPassword));
    }

    public boolean usernameExists(String username) {
        try {
            Passenger passenger = passengerDAO.findByUsername(username);
            return passenger != null;
        } catch (Exception e) {
            System.err.println("Error in usernameExists: " + e.getMessage());
            return false;
        }
    }

    public boolean emailExists(String email) {
        try {
            // Check if email exists in User or Passenger entity
            Passenger passenger = passengerDAO.findByEmail(email);
            return passenger != null;
        } catch (Exception e) {
            System.err.println("Error in emailExists: " + e.getMessage());
            return false;
        }
    }

    /// Delete passenger account
    public void deleteAccount(String username) {
        Passenger passenger = findByUsername(username);

        if (passenger == null) {
            throw new RuntimeException("Passenger not found");
        }

        passengerDAO.delete(passenger.getUserId());
    }
}