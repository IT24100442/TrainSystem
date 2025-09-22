package org.example.trainsystem.repository;

import jdk.jshell.Snippet;
import org.example.trainsystem.entity.TrainStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TrainStatusDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class TrainStatusRowMapper implements RowMapper<TrainStatus> {
        @Override
        public TrainStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            TrainStatus ts = new TrainStatus();
            ts.setStatusId(rs.getInt("statusId"));
            ts.setTrainRouteId(rs.getInt("trainRouteId"));
            ts.setStopId(rs.getInt("stopId"));
            try {
                ts.setStopName(rs.getString("stopName"));
            } catch (SQLException e) {
                ts.setStopName(null);
            }
            ts.setStatus(rs.getString("status"));
            ts.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return ts;
        }
    }

    public int save(TrainStatus status) {
        String sql = "INSERT INTO TrainStatus (trainRouteId, stopId, status, timestamp) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                status.getTrainRouteId(),
                status.getStopId(),
                status.getStatus(),
                status.getTimestamp());
    }

    public List<TrainStatus> findByTrainRoute(int trainRouteId) {
        String sql = """
            SELECT ts.statusId,
                   ts.trainRouteId,
                   ts.stopId,
                   s.stopName,
                   ts.status,
                   ts.timestamp
            FROM TrainStatus ts
            JOIN Stops s ON ts.stopId = s.stopId
            JOIN TrainRoute tr ON ts.trainRouteId = tr.trainRouteId
            JOIN Route r ON tr.routeId = r.routeId
            WHERE ts.trainRouteId = ?
            ORDER BY ts.timestamp DESC
        """;
        return jdbcTemplate.query(sql, new TrainStatusRowMapper(), trainRouteId);
    }


    public int find1ByTrainRoute(int trainRouteId){
        String sql = "SELECT statusId FROM TrainStatus WHERE trainRouteId = ?";
       try{
           return jdbcTemplate.queryForObject(sql, Integer.class, trainRouteId);
       } catch (Exception e){
           return -1;
       }
    }

    public int updateStatus(int statusId, String newStatus) {
        try {
            String sql = "UPDATE TrainStatus SET status = ?, timestamp = ? WHERE statusId = ?";
            System.out.println("Updating statusId " + statusId + " to new status: " + newStatus);
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            return jdbcTemplate.update(sql, newStatus, now,  statusId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int updateStop(int statusId, int stopId) {
        try {
            String sql = "UPDATE TrainStatus SET stopId = ?, timestamp = ? WHERE statusId = ?";
            System.out.println("Updating ststusId " + statusId + " to new stop: " + stopId);
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            return jdbcTemplate.update(sql, stopId, now,  statusId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<TrainStatus> findAll() {
        String sql = """
            SELECT ts.statusId,
                   ts.trainRouteId,
                   ts.stopId,
                   s.stopName,
                   r.routeName,
                   ts.status,
                   ts.timestamp
            FROM TrainStatus ts
            JOIN Stops s ON ts.stopId = s.stopId
            JOIN TrainRoute tr ON ts.trainRouteId = tr.trainRouteId
            JOIN Route r ON tr.routeId = r.routeId
            ORDER BY ts.timestamp DESC
        """;
        return jdbcTemplate.query(sql, new TrainStatusRowMapper());
    }

    public List<TrainStatus> findAllWithDetails() {
        return findAll();
    }
    public TrainStatus findById(int statusId) {
        String sql = """
        SELECT ts.statusId,
               ts.trainRouteId,
               ts.stopId,
               s.stopName,
               ts.status,
               ts.timestamp
        FROM TrainStatus ts
        JOIN Stops s ON ts.stopId = s.stopId
        WHERE ts.statusId = ?
    """;
        List<TrainStatus> results = jdbcTemplate.query(sql, new TrainStatusRowMapper(), statusId);
        return results.isEmpty() ? null : results.get(0);
    }

}