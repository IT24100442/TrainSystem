package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Stop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StopDAO {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class StopRowMapper implements RowMapper<Stop> {
        @Override
        public Stop mapRow(ResultSet rs, int rowNum) throws SQLException {
            Stop stop = new Stop();
            stop.setId(rs.getInt("stopId"));
            stop.setRouteId(rs.getInt("routeId"));
            stop.setStopName(rs.getString("stopName"));
            stop.setStopOrder(rs.getInt("stopOrder"));
            return stop;
        }
    }

    /**
     * Get all stops for a given route
     */
    public List<Stop> findByRouteId(int routeId) {
        String sql = "SELECT  stopId, routeId, stopName, stopOrder FROM Stops WHERE routeId = ? ORDER BY stopOrder ASC";
        return jdbcTemplate.query(sql, new StopRowMapper(), routeId);
    }

    /**
     * Get stop by stop ID
     */
    public Stop findById(int stopId) {
        String sql = "SELECT stopId, routeId, stopName, stopOrder FROM Stops WHERE stopId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new StopRowMapper(), stopId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Add a new stop
     */
    public int save(Stop stop) {
        String sql = "INSERT INTO Stops (routeId, stopName, stopOrder) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, stop.getRouteId(), stop.getStopName(), stop.getStopOrder());
    }

    /**
     * Update an existing stop
     */
    public int update(Stop stop) {
        String sql = "UPDATE Stops SET stopName = ?, stopOrder = ? WHERE stopId = ?";
        return jdbcTemplate.update(sql, stop.getStopName(), stop.getStopOrder(), stop.getId());
    }

    /**
     * Delete a stop
     */
    public int delete(int stopId) {
        String sql = "DELETE FROM Stops WHERE stopId = ?";
        return jdbcTemplate.update(sql, stopId);
    }
}
