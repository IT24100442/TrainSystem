package org.example.trainsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.trainsystem.entity.Route;
import org.example.trainsystem.entity.Stop;
import org.example.trainsystem.repository.RouteDAO;
import org.example.trainsystem.repository.StopDAO;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteDAO routeDAO;

    @Autowired
    private StopDAO stopDAO; // to fetch stops for a route

    /**
     * Get route assigned to a driver
     */
    public Route getRouteByDriverId(int driverId) {
        Route route = routeDAO.findByDriverId(driverId);
        if (route != null) {
            // Load stops for the route
            List<Stop> stops = stopDAO.findByRouteId(route.getRouteId());
            route.setStops(stops);
        }
        else
            System.out.println("Route not found in getRouteByDriverId");
        return route;
    }

    /**
     * Get route by route ID
     */
    public Route getRouteById(int routeId) {
        Route route = routeDAO.findById(routeId);
        if (route != null) {
            List<Stop> stops = stopDAO.findByRouteId(route.getRouteId());
            route.setStops(stops);
        }
        return route;
    }

    /**
     * Get all routes
     */
    public List<Route> getAllRoutes() {
        List<Route> routes = routeDAO.findAll();
        // Load stops for each route
        for (Route route : routes) {
            List<Stop> stops = stopDAO.findByRouteId(route.getRouteId());
            route.setStops(stops);
        }
        return routes;
    }

    /**
     * Save a new route
     */
    public int saveRoute(Route route) {
        return routeDAO.save(route);
    }

    /**
     * Update existing route
     */
    public int updateRoute(Route route) {
        return routeDAO.update(route);
    }

    /**
     * Delete a route by ID
     */
    public int deleteRoute(int routeId) {
        return routeDAO.delete(routeId);
    }
}
