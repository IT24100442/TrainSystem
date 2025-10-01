package org.example.trainsystem.dto;

public class ViolationReportRequest {
    private String officerId;
    private String trainId;
    private String passengerId;
    private String violationType;
    private String violationDescription;
    private Double penaltyAmount;
    private String reportStatus;

    // Constructors
    public ViolationReportRequest() {}

    public ViolationReportRequest(String officerId, String trainId, String violationType, String violationDescription) {
        this.officerId = officerId;
        this.trainId = trainId;
        this.violationType = violationType;
        this.violationDescription = violationDescription;
    }

    // Getters and Setters
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

    public Double getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public String getReportStatus() { return reportStatus; }
    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }
}