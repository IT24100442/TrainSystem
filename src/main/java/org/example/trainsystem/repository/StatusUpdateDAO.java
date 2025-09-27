package org.example.trainsystem.repository;

import org.example.trainsystem.entity.StatusUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class StatusUpdateDAO {

    @Autowired
    private static JdbcTemplate jdbcTemplate;

    private static final class StatusUpdateRowMapper implements RowMapper<StatusUpdate> {
        @Override
        public StatusUpdate mapRow(ResultSet rs, int rowNum) throws SQLException {
            StatusUpdate update = new StatusUpdate();
            update.setId(rs.getLong("id"));
            update.setTrainId(rs.getInt("trainId"));
            update.setCurrentLocation(rs.getString("currentLocation"));
            update.setStatus(rs.getString("status"));

            Timestamp timestamp = rs.getTimestamp("updateTime");
            if (timestamp != null) {
                update.setUpdateTime(timestamp.toLocalDateTime());
            }

            return update;
        }
    }

    public StatusUpdate findById(long id) {
        String sql = "SELECT id, trainId, currentLocation, status, updateTime FROM StatusUpdates WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new StatusUpdateRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }

    public static StatusUpdate findLatestByTrainId(int trainId) {
        String sql = """
            SELECT TOP 1 id, trainId, currentLocation, status, updateTime
            FROM StatusUpdates
            WHERE trainId = ?
            ORDER BY updateTime DESC
            """;
        try {
            return jdbcTemplate.queryForObject(sql, new StatusUpdateRowMapper(), trainId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<StatusUpdate> findByTrainId(int trainId) {
        String sql = """
            SELECT id, trainId, currentLocation, status, updateTime
            FROM StatusUpdates
            WHERE trainId = ?
            ORDER BY updateTime DESC
            """;
        return jdbcTemplate.query(sql, new StatusUpdateRowMapper(), trainId);
    }

    public static int save(StatusUpdate update) {
        String sql = """
            INSERT INTO StatusUpdates (trainId, currentLocation, status, updateTime)
            VALUES (?, ?, ?, ?)
            """;
        return jdbcTemplate.update(sql,
                update.getTrainId(),
                update.getCurrentLocation(),
                update.getStatus(),
                Timestamp.valueOf(update.getUpdateTime()));
    }

    public int update(StatusUpdate update) {
        String sql = """
            UPDATE StatusUpdates
            SET trainId = ?, currentLocation = ?, status = ?, updateTime = ?
            WHERE id = ?
            """;
        return jdbcTemplate.update(sql,
                update.getTrainId(),
                update.getCurrentLocation(),
                update.getStatus(),
                Timestamp.valueOf(update.getUpdateTime()),
                update.getId());
    }

    public int delete(long id) {
        String sql = "DELETE FROM StatusUpdates WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
