package org.example.trainsystem.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ViolationReport {
    private Integer violationId;
    private String officerId;
    private String trainId;
    private String passengerId;
    private String violationType;
    private String violationDescription;
    private Timestamp violationTime;
    private String reportStatus;
    private BigDecimal penaltyAmount;
    private String resolvedBy;
    private Timestamp resolutionTime;

    private TicketOfficer ticketOfficer; // Relationship with officer

    // Constructors
    public ViolationReport() {}

    public ViolationReport(String officerId, String trainId, String passengerId,
                           String violationType, String violationDescription, LocalDateTime violationTime) {
        this.officerId = officerId;
        this.trainId = trainId;
        this.passengerId = passengerId;
        this.violationType = violationType;
        this.violationDescription = violationDescription;
        this.violationTime = Timestamp.valueOf(violationTime);
    }

    // Getters and Setters
    public Integer getViolationId() { return violationId; }
    public void setViolationId(Integer violationId) { this.violationId = violationId; }

    public String getOfficerId() { return officerId; }
    public void setOfficerId(String officerId) { this.officerId = officerId; }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }

    public String getViolationType() { return violationType; }
    public void setViolationType(String violationType) { this.violationType = violationType; }

    public String getViolationDescription() { return violationDescription; }
    public void setViolationDescription(String violationDescription) { this.violationDescription = violationDescription; }

    public Timestamp getViolationTime() { return violationTime; }
    public void setViolationTime(Timestamp violationTime) { this.violationTime = violationTime; }

    public String getReportStatus() { return reportStatus; }
    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }

    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(BigDecimal penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public Timestamp getResolutionTime() { return resolutionTime; }
    public void setResolutionTime(Timestamp resolutionTime) { this.resolutionTime = resolutionTime; }

    public TicketOfficer getTicketOfficer() { return ticketOfficer; }
    public void setTicketOfficer(TicketOfficer ticketOfficer) { this.ticketOfficer = ticketOfficer; }
}
