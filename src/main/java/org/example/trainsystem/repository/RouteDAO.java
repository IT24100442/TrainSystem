package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Route;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RouteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class RouteRowMapper implements RowMapper<Route> {
        @Override
        public Route mapRow(ResultSet rs, int rowNum) throws SQLException {
            Route route = new Route();
            route.setRid(rs.getString("rid"));
            route.setRouteName(rs.getString("routeName"));
            route.setDuration(rs.getInt("duration"));
            route.setRouteStart(rs.getString("routeStart"));
            route.setRouteDestination(rs.getString("routeDestination"));
            route.setDriverId(rs.getString("driverId"));
            return route;
        }
    }

    public Route findById(String rid) {
        String sql = "SELECT rid, routeName, duration, routeStart, routeDestination, driverId FROM Route WHERE rid = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new RouteRowMapper(), rid);
        } catch (Exception e) {
            return null;
        }
    }

    public Route findByDriverId(String driverId) {
        String sql = "SELECT rid, routeName, duration, routeStart, routeDestination, driverId FROM Route WHERE driverId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new RouteRowMapper(), driverId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Route> findAll() {
        String sql = "SELECT rid, routeName, duration, routeStart, routeDestination, driverId FROM Route";
        return jdbcTemplate.query(sql, new RouteRowMapper());
    }

    public int save(Route route) {
        String sql = "INSERT INTO Route (rid, routeName, duration, routeStart, routeDestination, driverId) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, route.getRid(), route.getRouteName(), route.getDuration(),
                route.getRouteStart(), route.getRouteDestination(), route.getDriverId());
    }

    public int update(Route route) {
        String sql = "UPDATE Route SET routeName = ?, duration = ?, routeStart = ?, routeDestination = ?, driverId = ? WHERE rid = ?";
        return jdbcTemplate.update(sql, route.getRouteName(), route.getDuration(), route.getRouteStart(),
                route.getRouteDestination(), route.getDriverId(), route.getRid());
    }

    public int delete(String rid) {
        String sql = "DELETE FROM Route WHERE rid = ?";
        return jdbcTemplate.update(sql, rid);
    }
}