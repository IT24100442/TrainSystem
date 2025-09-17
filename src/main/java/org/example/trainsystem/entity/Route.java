package org.example.trainsystem.entity;

public class Route {
    private String rid;
    private String routeName;
    private Integer duration;
    private String routeStart;
    private String routeDestination;
    private String driverId;

    // Constructors
    public Route() {}

    public Route(String rid, String routeName, Integer duration, String routeStart, String routeDestination, String driverId) {
        this.rid = rid;
        this.routeName = routeName;
        this.duration = duration;
        this.routeStart = routeStart;
        this.routeDestination = routeDestination;
        this.driverId = driverId;
    }

    // Getters and Setters
    public String getRid() { return rid; }
    public void setRid(String rid) { this.rid = rid; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getRouteStart() { return routeStart; }
    public void setRouteStart(String routeStart) { this.routeStart = routeStart; }

    public String getRouteDestination() { return routeDestination; }
    public void setRouteDestination(String routeDestination) { this.routeDestination = routeDestination; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

}
