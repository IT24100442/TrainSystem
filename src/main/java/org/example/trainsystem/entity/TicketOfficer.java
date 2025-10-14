package org.example.trainsystem.entity;

public class TicketOfficer {
    private int userId;
    private int trainId;
    private User user; // Relationship with User entity

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructors
    public TicketOfficer() {}

    public TicketOfficer(int userId, int trainId) {
        this.userId = userId;
        this.trainId = trainId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }
}