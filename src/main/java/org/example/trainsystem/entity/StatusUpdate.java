package org.example.trainsystem.entity;
import java.time.LocalDateTime;

public class StatusUpdate {

    private Long id;
    private String trainId;
    private String currentLocation;
    private String status;
    private String remarks;
    private LocalDateTime updateTime;

    // Constructors
    public StatusUpdate() {}

    public StatusUpdate(String trainId, String currentLocation, String status, String remarks, LocalDateTime updateTime) {
        this.trainId = trainId;
        this.currentLocation = currentLocation;
        this.status = status;
        this.remarks = remarks;
        this.updateTime = updateTime;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

}
