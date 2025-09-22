package org.example.trainsystem.repository;

import org.example.trainsystem.entity.ITOfficer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ITOfficerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static class ITOfficerRowMapper implements RowMapper<ITOfficer> {
        @Override
        public ITOfficer mapRow(ResultSet rs, int rowNum) throws SQLException {
            ITOfficer officer = new ITOfficer();
            officer.setUserId(rs.getInt("userId"));
            officer.setAccessLevel(rs.getString("accessLevel"));
            return officer;
        }
    }

    public int save(ITOfficer officer) {
        String sql = "INSERT INTO ItOfficer (userId, accessLevel) VALUES (?, ?)";
        return jdbcTemplate.update(sql,
                officer.getUserId(),
                officer.getAccessLevel());
    }

    public ITOfficer findById(int userId) {
        String sql = "SELECT userId, accessLevel FROM ItOfficer WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ITOfficerRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    public ITOfficer findByUserId(int userId) {
        return findById(userId);
    }

    public List<ITOfficer> findAll() {
        String sql = "SELECT userId, accessLevel FROM ItOfficer";
        return jdbcTemplate.query(sql, new ITOfficerRowMapper());
    }

    public List<ITOfficer> findByAccessLevel(String accessLevel) {
        String sql = "SELECT userId, accessLevel FROM ItOfficer WHERE accessLevel = ?";
        return jdbcTemplate.query(sql, new ITOfficerRowMapper(), accessLevel);
    }

    public int update(ITOfficer officer) {
        String sql = "UPDATE ItOfficer SET accessLevel = ? WHERE userId = ?";
        return jdbcTemplate.update(sql,
                officer.getAccessLevel(),
                officer.getUserId());
    }

    public int delete(int userId) {
        String sql = "DELETE FROM ItOfficer WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    public boolean existsByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM ItOfficer WHERE userId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }
}