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
     * Users fields: name, email, username
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

        // Update fields from Users class (using inherited setters)
        passenger.setName(name);
        passenger.setEmail(email);

        // Update field from Passenger class
        passenger.setAddress(address);

        // If you have a phone field in Passenger entity, update it
        // passenger.setPhone(phone);

        passengerDAO.update(passenger);
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
        if (newPassword.length() < 8) {
            throw new RuntimeException("New password must be at least 8 characters long");
        }

        // Update password (using inherited setter)
        passenger.setPassword(passwordEncoder.encode(newPassword));
        passengerDAO.update(passenger);
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