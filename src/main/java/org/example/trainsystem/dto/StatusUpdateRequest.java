package org.example.trainsystem.dto;

public class StatusUpdateRequest {
    private String location;
    private String status;
    private String remarks;

    // Constructors
    public StatusUpdateRequest() {}

    public StatusUpdateRequest(String location, String status, String remarks) {
        this.location = location;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}