package org.example.trainsystem.entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class ViolationReport {
    private int reportId;
    private int ticketOfficerId;
    private int trainId;
    private int passengerId;
    private String violationType;
    private LocalDateTime reportDate;


    private TicketOfficer ticketOfficer; // Relationship with officer
    private String status;

    // Constructors
    public ViolationReport() {
    }

    public ViolationReport(int reportId, int officerId, int trainId, int passengerId,
                           String violationType, LocalDateTime violationTime) {
        this.reportId = reportId;
        this.ticketOfficerId = officerId;
        this.trainId = trainId;
        this.passengerId = passengerId;
        this.reportDate = violationTime;
        this.violationType = violationType;

    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getTicketOfficerId() {
        return ticketOfficerId;
    }

    public void setTicketOfficerId(int ticketOfficerId) {
        this.ticketOfficerId = ticketOfficerId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getViolationType() {
        return violationType;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public TicketOfficer getTicketOfficer() {
        return ticketOfficer;
    }

    public void setTicketOfficer(TicketOfficer ticketOfficer) {
        this.ticketOfficer = ticketOfficer;
    }


    public OffsetDateTime getReportedDate() {
        return reportDate.atOffset(OffsetDateTime.now().getOffset());
    }

    public String getStatus() {
        return "PENDING";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

