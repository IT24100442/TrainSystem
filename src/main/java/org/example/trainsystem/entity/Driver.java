package org.example.trainsystem.entity;

public class Driver {
    private int userId;
    private String license;
    private User user; // For joined data
    private int trainId; // add this


    // Constructors
    public Driver() {}

    public Driver(int userId, String license) {
        this.userId = userId;
        this.license = license;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }

    public String getName() { return user != null ? user.getName() : null; }

}
