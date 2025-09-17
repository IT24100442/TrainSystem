package org.example.trainsystem.entity;


import java.util.ArrayList;
import java.util.List;

public class Route {
    private int routeId;
    private String routeName;
    private int durationMinutes;
    private Integer driverId;
    private List<Stop> stops = new ArrayList<>();

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public int getDurationMinutes() { return durationMinutes; }   // <- fix getter
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public List<Stop> getStops() { return stops; }  // always initialize!
    public void setStops(List<Stop> stops) { this.stops = stops; }

}
