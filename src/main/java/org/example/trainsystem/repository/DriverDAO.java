package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Driver;
import org.example.trainsystem.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DriverDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class DriverRowMapper implements RowMapper<Driver> {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setUserId(rs.getString("userId"));
            driver.setLicense(rs.getString("license"));
            return driver;
        }
    }

    private static final class DriverWithUserRowMapper implements RowMapper<Driver> {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setUserId(rs.getString("userId"));
            driver.setLicense(rs.getString("license"));

            User user = new User();
            user.setUserId(rs.getString("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));

            driver.setUser(user);
            return driver;
        }
    }

    public Driver findById(String userId) {
        String sql = "SELECT userId, license FROM Driver WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new DriverRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    public Driver findDriverWithUser(String username) {
        String sql = """
        SELECT d.userId, d.license, u.username, u.password, u.email, u.name, u.userType
        FROM Driver d
        INNER JOIN Users u ON d.userId = u.userId
        WHERE u.username = ?;
        
    """;
        try {
            return jdbcTemplate.queryForObject(sql, new DriverWithUserRowMapper(), username);
        } catch (Exception e) {
            return null;
        }
    }


    public int save(Driver driver) {
        String sql = "INSERT INTO Driver (userId, license) VALUES (?, ?)";
        return jdbcTemplate.update(sql, driver.getUserId(), driver.getLicense());
    }

    public int update(Driver driver) {
        String sql = "UPDATE Driver SET license = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, driver.getLicense(), driver.getUserId());
    }

    public int delete(String userId) {
        String sql = "DELETE FROM Driver WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }
}