package org.example.trainsystem.entity;

public class Stop {
    private Integer stopId;
    private Integer routeId;
    private String stopName;
    private int stopOrder;


    public Stop() {
    }

    // Add this constructor
    public Stop(Integer id, String stopName, int stopOrder) {
        this.stopId = id;
        this.stopName = stopName;
        this.stopOrder = stopOrder;
    }

    // Optional constructor including routeId
    public Stop(Integer id, String stopName, int stopOrder, int routeId) {
        this.stopId = id;
        this.stopName = stopName;
        this.stopOrder = stopOrder;
        this.routeId = routeId;
    }

    // Getters and setters
    public Integer getId() { return stopId; }
    public void setId(Integer id) { this.stopId = id; }

    public Integer getRouteId() { return routeId; }
    public void setRouteId(Integer routeId) { this.routeId = routeId; }

    public String getStopName() { return stopName; }
    public void setStopName(String stopName) { this.stopName = stopName; }

    public int getStopOrder() { return stopOrder; }
    public void setStopOrder(int stopOrder) { this.stopOrder = stopOrder; }


}
