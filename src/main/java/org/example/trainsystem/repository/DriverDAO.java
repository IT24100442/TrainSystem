package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Driver;
import org.example.trainsystem.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DriverDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class DriverRowMapper implements RowMapper<Driver> {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setUserId(rs.getInt("userId"));
            driver.setLicense(rs.getString("license"));
            driver.setTrainId(rs.getInt("trainId"));

            return driver;
        }
    }

    private static final class DriverWithUserRowMapper implements RowMapper<Driver> {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setUserId(rs.getInt("userId"));
            driver.setLicense(rs.getString("license"));
            driver.setTrainId(rs.getInt("trainId"));

            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));


            driver.setUser(user);
            return driver;
        }
    }

    public Driver findById(int userId) {
        String sql = "SELECT userId, license, trainId FROM Driver WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new DriverRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    public Driver findDriverWithUser(String username) {
        String sql = """
        SELECT d.userId, d.license, d.trainId, u.username, u.password, u.email, u.name, u.userType
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
        String sql = "INSERT INTO Driver (userId, license, trainId) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, driver.getUserId(), driver.getLicense(), driver.getTrainId());
    }

    public int update(Driver driver) {
        String sql = "UPDATE Driver SET license = ? ,trainId = ?  WHERE userId = ?";
        return jdbcTemplate.update(sql, driver.getLicense(), driver.getTrainId(), driver.getUserId());
    }

    public int delete(int userId) {
        String sql = "DELETE FROM Driver WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // Fetch all drivers
    public List<Driver> findAllDrivers() {
        String sql = "SELECT userId, license, trainId FROM Driver";
        return jdbcTemplate.query(sql, new DriverRowMapper());
    }
    public Driver findDriverById(int userId) {
        String sql = "SELECT userId, trainId, license FROM Driver WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new DriverRowMapper(), userId);
        } catch (Exception e) {
            return null; // Return null if not found
        }
    }
}
