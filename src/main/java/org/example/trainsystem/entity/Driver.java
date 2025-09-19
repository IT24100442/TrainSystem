package org.example.trainsystem.entity;




public class Driver {
    private int userId;
    private String license;
    private User user; // For joined data
    private int trainId; // add this
    private Route assignedRoute;
    private int currentStopIndex = 0;


    // Constructors
    public Driver() {}

    public Driver(int userId, String license) {
        this.userId = userId;
        this.license = license;
    }

    public Driver(int userId, String license, User user, int trainId, Route assignedRoute, int currentStopIndex) {
        this.userId = userId;
        this.license = license;
        this.user = user;
        this.trainId = trainId;
        this.assignedRoute = assignedRoute;
        this.currentStopIndex = currentStopIndex;
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


    public boolean moveToNextStop() {
        if (assignedRoute != null && currentStopIndex < assignedRoute.getStops().size() - 1) {
            currentStopIndex++;
            return true;
        }
        return false; // already at final stop
    }
}

