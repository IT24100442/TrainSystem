package org.example.trainsystem.entity;

import org.springframework.boot.autoconfigure.security.SecurityProperties;

public class TicketOfficer {
    private String userId;
    private String badgeNumber;
    private String assignedRoute;
    private SecurityProperties.User user; // Reference your own User entity

    // Constructors
    public TicketOfficer() {}

    public TicketOfficer(String userId, String badgeNumber, String assignedRoute) {
        this.userId = userId;
        this.badgeNumber = badgeNumber;
        this.assignedRoute = assignedRoute;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBadgeNumber() { return badgeNumber; }
    public void setBadgeNumber(String badgeNumber) { this.badgeNumber = badgeNumber; }

    public String getAssignedRoute() { return assignedRoute; }
    public void setAssignedRoute(String assignedRoute) { this.assignedRoute = assignedRoute; }

    public SecurityProperties.User getUser() { return user; }
    public void setUser(SecurityProperties.User user) { this.user = user; }

    public String getName() {
        return user != null ? user.getName() : null;
    }
}