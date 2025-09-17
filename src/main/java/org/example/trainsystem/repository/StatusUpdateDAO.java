package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.StatusUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class StatusUpdateDAO {

    @Autowired
    private static JdbcTemplate jdbcTemplate;

    private static final class LocationUpdateRowMapper implements RowMapper<StatusUpdate> {
        @Override
        public StatusUpdate mapRow(ResultSet rs, int rowNum) throws SQLException {
            StatusUpdate locationUpdate = new StatusUpdate();
            locationUpdate.setId(rs.getLong("id"));
            locationUpdate.setTrainId(rs.getString("trainId"));
            locationUpdate.setCurrentLocation(rs.getString("currentLocation"));
            locationUpdate.setStatus(rs.getString("status"));
            locationUpdate.setRemarks(rs.getString("remarks"));

            Timestamp timestamp = rs.getTimestamp("updateTime");
            if (timestamp != null) {
                locationUpdate.setUpdateTime(timestamp.toLocalDateTime());
            }

            return locationUpdate;
        }
    }

    public StatusUpdate findById(Long id) {
        String sql = "SELECT id, trainId, currentLocation, status, remarks, updateTime FROM LocationUpdates WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new LocationUpdateRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }

    public static StatusUpdate findLatestByTrainId(String trainId) {
        String sql = "SELECT TOP 1 id, trainId, currentLocation, status, remarks, updateTime FROM LocationUpdates WHERE trainId = ? ORDER BY updateTime DESC";
        try {
            return jdbcTemplate.queryForObject(sql, new LocationUpdateRowMapper(), trainId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<StatusUpdate> findByTrainId(String trainId) {
        String sql = "SELECT id, trainId, currentLocation, status, remarks, updateTime FROM LocationUpdates WHERE trainId = ? ORDER BY updateTime DESC";
        return jdbcTemplate.query(sql, new LocationUpdateRowMapper(), trainId);
    }

    public static int save(StatusUpdate locationUpdate) {
        String sql = "INSERT INTO LocationUpdates (trainId, currentLocation, status, remarks, updateTime) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, locationUpdate.getTrainId(), locationUpdate.getCurrentLocation(),
                locationUpdate.getStatus(), locationUpdate.getRemarks(),
                Timestamp.valueOf(locationUpdate.getUpdateTime()));
    }

    public int update(StatusUpdate locationUpdate) {
        String sql = "UPDATE LocationUpdates SET trainId = ?, currentLocation = ?, status = ?, remarks = ?, updateTime = ? WHERE id = ?";
        return jdbcTemplate.update(sql, locationUpdate.getTrainId(), locationUpdate.getCurrentLocation(),
                locationUpdate.getStatus(), locationUpdate.getRemarks(),
                Timestamp.valueOf(locationUpdate.getUpdateTime()), locationUpdate.getId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM LocationUpdates WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}