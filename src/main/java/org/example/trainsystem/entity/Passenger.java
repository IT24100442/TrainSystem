package org.example.trainsystem.entity;

public class Passenger {
    private int userId;
    private String address;

    public Passenger() {
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
