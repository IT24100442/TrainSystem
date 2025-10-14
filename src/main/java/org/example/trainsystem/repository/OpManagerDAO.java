package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.OpManager;
import org.example.trainsystem.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OpManagerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class DriverRowMapper implements RowMapper<OpManager> {
        @Override
        public OpManager mapRow(ResultSet rs, int rowNum) throws SQLException {
            OpManager opManager = new OpManager();
            opManager.setUserId(rs.getInt("userId"));
            opManager.setContactNumber(rs.getString("contactNumber"));

            return opManager;
        }
    }

    private static final class DriverWithUserRowMapper implements RowMapper<OpManager> {
        @Override
        public OpManager mapRow(ResultSet rs, int rowNum) throws SQLException {
            OpManager opManager = new OpManager();
            opManager.setUserId(rs.getInt("userId"));
            opManager.setContactNumber(rs.getString("contactNumber"));

            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));


            opManager.setUser(user);
            return opManager;
        }
    }

    public OpManager findById(int userId) {
        String sql = "SELECT userId, contactNumber FROM OpManager WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new DriverRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    public OpManager findOpManagerWithUser(String username) {
        String sql = """
        SELECT o.userId, o.contactNumber, u.username, u.password, u.email, u.name, u.userType
        FROM OpManager o
        INNER JOIN Users u ON o.userId = u.userId
        WHERE u.username = ?;
        """;
        try {
            return jdbcTemplate.queryForObject(sql, new DriverWithUserRowMapper(), username);
        } catch (Exception e) {
            return null;
        }
    }

    public int save(OpManager opManager) {
        String sql = "INSERT INTO OpManager (userId, contactNumber) VALUES (?, ?)";
        return jdbcTemplate.update(sql, opManager.getUserId(), opManager.getContactNumber());
    }

    public int update(OpManager opManager) {
        String sql = "UPDATE OpManager SET contactNumber = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, opManager.getContactNumber(), opManager.getUserId());
    }

    public int delete(int userId) {
        String sql = "DELETE FROM OpManager WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // Fetch all drivers
    public List<OpManager> findAllOpManagers() {
        String sql = "SELECT userId,contactNumber FROM OpManager";
        return jdbcTemplate.query(sql, new DriverRowMapper());
    }
    public OpManager findOpManagerById(int userId) {
        String sql = "SELECT userId, contactNumber FROM OpManager WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new DriverRowMapper(), userId);
        } catch (Exception e) {
            return null; // Return null if not found
        }
    }
}
