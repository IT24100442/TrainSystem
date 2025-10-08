package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Booking;
import org.example.trainsystem.entity.Concern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ConcernDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class ConcernRowMapper implements RowMapper<Concern> {
        @Override
        public Concern mapRow(ResultSet rs, int rowNum) throws SQLException {
            Concern concern = new Concern();
            concern.setConcernId(rs.getInt("concernId"));
            concern.setDescription(rs.getString("description"));
            concern.setStatus(rs.getString("status"));
            concern.setPassengerId(rs.getInt("passengerId"));





            Timestamp timestamp = rs.getTimestamp("date_submitted");
            if (timestamp != null) {
                concern.setDate_submitted(timestamp.toLocalDateTime());
            }

            return concern;
        }

    }

    public int save(Concern concern) {
        String sql = "INSERT INTO Concern (description, status, passengerId, date_submitted) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, concern.getDescription(), concern.getStatus(), concern.getPassengerId(), Timestamp.valueOf(concern.getDate_submitted()));
    }

    public Concern findById(int concernId) {
        String sql = "SELECT * FROM Concern WHERE concernId = ?";
        return jdbcTemplate.queryForObject(sql, new ConcernRowMapper(), concernId);
    }

    public int updateStatus(int concernId, String status) {
        String sql = "UPDATE Concern SET status = ? WHERE concernId = ?";
        return jdbcTemplate.update(sql, status, concernId);
    }

    public List<Concern> findAll() {
        String sql = "SELECT * FROM Concern";
        return jdbcTemplate.query(sql, new ConcernRowMapper());
    }
}
