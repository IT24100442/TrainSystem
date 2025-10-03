package org.example.trainsystem.entity;

public class CustomerServiceExecutive extends User{
    private int userId;
    private String contactNum;
    private User user; // Relationship with User entity

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CustomerServiceExecutive() {

    }

    public CustomerServiceExecutive(int userId, String username, String password, String email, String name, String userType, int userId1, String contactNum) {
        super(userId, username, password, email, name, userType);
        this.userId = userId1;
        this.contactNum = contactNum;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }
}
