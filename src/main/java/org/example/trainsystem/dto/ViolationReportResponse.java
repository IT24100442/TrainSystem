package org.example.trainsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ViolationReportResponse {
    private Integer violationId;
    private String officerId;
    private String trainId;
    private String passengerId;
    private String violationType;
    private String violationDescription;
    private LocalDateTime violationTime;
    private String reportStatus;
    private BigDecimal penaltyAmount;
    private String resolvedBy;
    private LocalDateTime resolutionTime;

    private String officerName;
    private String officerBadgeNumber;
    private String officerAssignedRoute;

    // Constructors
    public ViolationReportResponse() {}

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

    public LocalDateTime getViolationTime() { return violationTime; }
    public void setViolationTime(LocalDateTime violationTime) { this.violationTime = violationTime; }

    public String getReportStatus() { return reportStatus; }
    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }

    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(BigDecimal penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    public LocalDateTime getResolutionTime() { return resolutionTime; }
    public void setResolutionTime(LocalDateTime resolutionTime) { this.resolutionTime = resolutionTime; }

    public String getOfficerName() { return officerName; }
    public void setOfficerName(String officerName) { this.officerName = officerName; }

    public String getOfficerBadgeNumber() { return officerBadgeNumber; }
    public void setOfficerBadgeNumber(String officerBadgeNumber) { this.officerBadgeNumber = officerBadgeNumber; }

    public String getOfficerAssignedRoute() { return officerAssignedRoute; }
    public void setOfficerAssignedRoute(String officerAssignedRoute) { this.officerAssignedRoute = officerAssignedRoute; }
}
