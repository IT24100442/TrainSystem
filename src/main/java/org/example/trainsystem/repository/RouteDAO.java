package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Stop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Route;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class RouteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Route findByOfficerId(String officerId) {
     return null;}

    private static final class RouteRowMapper implements RowMapper<Route> {
        @Override
        public Route mapRow(ResultSet rs, int rowNum) throws SQLException {
            Route route = new Route();
            route.setRouteId(rs.getInt("routeId"));
            route.setRouteName(rs.getString("routeName"));
            route.setDurationMinutes(rs.getInt("durationMinutes"));
            route.setDriverId(rs.getInt("driverId"));
            return route;
        }
    }

    public Route findById(int rid) {
        String sql = "SELECT routeId, routeName, durationMinutes, driverId FROM Route WHERE rid = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new RouteRowMapper(), rid);
        } catch (Exception e) {
            return null;
        }
    }

    public Route findByDriverId(int driverId) {
        String sql = "SELECT TOP 1 routeId, routeName, durationMinutes, driverId FROM Route WHERE driverId = ? ";
        try {
            System.out.println(driverId);
            return jdbcTemplate.queryForObject(sql, new RouteRowMapper(), driverId);

        } catch (Exception e) {
            return null;
        }
    }
    public Route findByName(String routeName) {
        String sql = "SELECT * FROM Route WHERE routeName=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Route r = new Route();
            r.setRouteId(rs.getInt("routeId"));
            r.setRouteName(rs.getString("routeName"));
            r.setDurationMinutes(rs.getInt("durationMinutes"));
            r.setDriverId(rs.getObject("driverId") != null ? rs.getInt("driverId") : null);
            return r;
        }, routeName);
    }


    public List<Route> findAll() {
        String sql = "SELECT routeId, routeName, durationMinutes, driverId FROM Route";
        return jdbcTemplate.query(sql, new RouteRowMapper());
    }

    // Insert with auto-generated rid
    public int save(Route route) {
        String sql = "INSERT INTO Route (routeName, durationMinutes, driverId) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, route.getRouteName());
            ps.setInt(2, route.getDurationMinutes());
            ps.setInt(3, route.getDriverId());
            return ps;
        }, keyHolder);

        int generatedId = keyHolder.getKey().intValue();
        route.setRouteId(generatedId);
        return generatedId;
    }

    public int update(Route route) {
        String sql = "UPDATE Route SET routeName = ?, durationMinutes = ?, driverId = ? WHERE rid = ?";
        return jdbcTemplate.update(sql,
                route.getRouteName(),
                route.getDurationMinutes(),
                route.getDriverId(),
                route.getRouteName());
    }

    public int delete(int rid) {
        String sql = "DELETE FROM Route WHERE routeId = ?";
        return jdbcTemplate.update(sql, rid);
    }

    public List<Stop> getStopsByRouteId(Integer rid) {
        String sql = "SELECT routeId, stopName, stopOrder FROM Stops WHERE routeId = ? ORDER BY stopOrder ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Stop(
                rs.getInt("id"),
                rs.getString("stopName"),
                rs.getInt("stopOrder")
        ), rid);
    }
}
