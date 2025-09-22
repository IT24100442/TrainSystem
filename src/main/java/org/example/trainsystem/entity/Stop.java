package org.example.trainsystem.entity;

public class Stop {
    private int stopId;
    private int routeId;
    private String stopName;
    private int stopOrder;


    public Stop() {
    }

    // Add this constructor
    public Stop(int id, String stopName, int stopOrder) {
        this.stopId = id;
        this.stopName = stopName;
        this.stopOrder = stopOrder;
    }

    // Optional constructor including routeId
    public Stop(int id, String stopName, int stopOrder, int routeId) {
        this.stopId = id;
        this.stopName = stopName;
        this.stopOrder = stopOrder;
        this.routeId = routeId;
    }

    // Getters and setters
    public int getId() { return stopId; }
    public void setId(int id) { this.stopId = id; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public String getStopName() { return stopName; }
    public void setStopName(String stopName) { this.stopName = stopName; }

    public int getStopOrder() { return stopOrder; }
    public void setStopOrder(int stopOrder) { this.stopOrder = stopOrder; }
}
