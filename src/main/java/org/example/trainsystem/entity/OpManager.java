package org.example.trainsystem.entity;

public class OpManager {
    private Integer userId;
    private String contactNumber;
    private User user;

    // Constructors
    public OpManager() {}

    public OpManager(String contactNumber, User user) {
        this.contactNumber = contactNumber;
        this.user = user;
        this.userId = user.getUserId();
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getUserId();
        }
    }
}