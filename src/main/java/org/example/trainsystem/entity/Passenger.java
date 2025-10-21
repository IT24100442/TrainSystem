package org.example.trainsystem.entity;

public class Passenger extends User {
    private int userId;
    private String address;

    public Passenger() {
    }

    public Passenger(int userId, String username, String password, String email, String name, String userType, int userId1, String address) {
        super(userId, username, password, email, name, userType);
        this.userId = userId1;
        this.address = address;
    }

    public Passenger(int userId, String address) {
        this.userId = userId;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
