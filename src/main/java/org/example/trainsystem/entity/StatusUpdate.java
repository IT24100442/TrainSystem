package org.example.trainsystem.entity;
import java.time.LocalDateTime;

public class StatusUpdate {

    private Long id;
    private int trainId;
    private String currentLocation;
    private String status;
    private LocalDateTime updateTime;

    // Constructors
    public StatusUpdate() {}

    public StatusUpdate(int trainId, String currentLocation, String status, LocalDateTime updateTime) {
        this.trainId = trainId;
        this.currentLocation = currentLocation;
        this.status = status;
        this.updateTime = updateTime;
    }

    public StatusUpdate(int trainId, String nextStop, String onSchedule) {
        this.trainId = trainId;
        this.currentLocation = nextStop;
        this.status = onSchedule;
    }


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

}
