package org.example.trainsystem.entity;

import java.time.LocalDateTime;

public class TrainStatus {

    private int statusId;        // primary key
    private Integer trainRouteId;    // links a train to a route
    private String stopName;          // which stop the update is for
    private int stopId;
    private String routeName;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    private String status;       // ON_TIME, DELAYED, CANCELLED
    private LocalDateTime timestamp; // when the status was recorded

    // --- Getters and Setters ---
    public int getStatusId() {
        return statusId;
    }
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Integer getTrainRouteId() {
        return trainRouteId;
    }
    public void setTrainRouteId(Integer trainRouteId) {
        this.trainRouteId = trainRouteId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }



    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
