package org.example.trainsystem.entity;

public class Passenger extends User {
    private String address;

    public Passenger() {
    }

    public Passenger(int userId, String username, String password, String email, String name, String userType, String address) {
        super(userId, username, password, email, name, userType);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }



}
