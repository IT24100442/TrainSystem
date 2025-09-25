package org.example.trainsystem.dto;

public class PassengerDTO {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String name;
    private String userType;
    private String address;

    // Default constructor
    public PassengerDTO() {}

    // Convenience constructor
    public PassengerDTO(int userId, String username, String password, String email, String name, String userType, String address) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.userType = userType;
        this.address = address;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
