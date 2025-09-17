package org.example.trainsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.trainsystem.repository.UserDAO;
import org.example.trainsystem.entity.User;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public List<User> getOperationsManagers() {
        return userDAO.findByUserType("OpManager");
    }

    public User getUserById(String userId) {
        return userDAO.findById(userId);
    }

    public void createUser(User user) {
        int result = userDAO.save(user);
        if (result == 0) {
            throw new RuntimeException("Failed to create user");
        }
    }

    public void updateUser(User user) {
        int result = userDAO.update(user);
        if (result == 0) {
            throw new RuntimeException("Failed to update user");
        }
    }

    public void deleteUser(String userId) {
        int result = userDAO.delete(userId);
        if (result == 0) {
            throw new RuntimeException("Failed to delete user");
        }
    }
}