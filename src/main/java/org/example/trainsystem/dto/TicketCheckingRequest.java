package org.example.trainsystem.dto;

public class TicketCheckingRequest {
    private String bookingId;
    private String status;
    private String officerId;
    private String trainId;
    private String remarks;


    public TicketCheckingRequest() {}

    public TicketCheckingRequest(String bookingId, String status) {
        this.bookingId = bookingId;
        this.status = status;
    }

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOfficerId() { return officerId; }
    public void setOfficerId(String officerId) { this.officerId = officerId; }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}