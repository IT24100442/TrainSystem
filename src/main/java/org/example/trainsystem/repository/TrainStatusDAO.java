package org.example.trainsystem.repository;

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

            // Stop name may not be in all queries
            try {
                ts.setStopName(rs.getString("stopName"));
            } catch (SQLException e) {
                ts.setStopName(null);
            }

            // Route name may not be in all queries
            try {
                ts.setRouteName(rs.getString("routeName"));
            } catch (SQLException e) {
                ts.setRouteName(null);
            }

            ts.setStatus(rs.getString("status"));
            ts.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return ts;
        }
    }

    public int save(TrainStatus status) {
        String sql = "INSERT INTO TrainStatus (trainRouteId, stopId, status) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                status.getTrainRouteId(),
                status.getStopId(),
                status.getStatus());
    }

    public List<TrainStatus> findByTrainRoute(int trainRouteId) {
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
            WHERE ts.trainRouteId = ?
            ORDER BY ts.timestamp DESC
        """;
        return jdbcTemplate.query(sql, new TrainStatusRowMapper(), trainRouteId);
    }

    public int updateStatus(int statusId, String newStatus) {
        String sql = "UPDATE TrainStatus SET status = ? WHERE statusId = ?";
        return jdbcTemplate.update(sql, newStatus, statusId);
    }

    public List<TrainStatus> findAll() {
        // Make this consistent with findAllWithDetails
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
        return findAll(); // no need to duplicate
    }
}