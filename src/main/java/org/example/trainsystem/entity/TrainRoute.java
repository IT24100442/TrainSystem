package org.example.trainsystem.entity;

public class TrainRoute {
    private Integer trainRouteId;
    private int trainId;
    private int routeId;
    private boolean active;

    // getters & setters
    public Integer getTrainRouteId() { return trainRouteId; }
    public void setTrainRouteId(Integer trainRouteId) { this.trainRouteId = trainRouteId; }

    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
