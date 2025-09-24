package org.example.trainsystem.entity;

public class ITOfficer {
    int userId;
    String accessLevel;

    public ITOfficer() {
    }

    public ITOfficer(int userId, String accessLevel) {
        this.userId = userId;
        this.accessLevel = accessLevel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
}
