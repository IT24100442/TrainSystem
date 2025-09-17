package org.example.trainsystem.entity;




public class Driver {
    private String userId;
    private String license;
    private User user; // For joined data

    // Constructors
    public Driver() {}

    public Driver(String userId, String license) {
        this.userId = userId;
        this.license = license;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return user != null ? user.getName() : null; }

}
