package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PassengerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class PassengerRowMapper implements RowMapper<Passenger> {
        @Override
        public Passenger mapRow(ResultSet rs, int rowNum) throws SQLException {
            Passenger passenger = new Passenger();
            passenger.setUserId(rs.getInt("userId"));
            passenger.setUsername(rs.getString("username"));
            passenger.setName(rs.getString("name"));
            passenger.setEmail(rs.getString("email"));
            passenger.setUserType(rs.getString("userType"));
            passenger.setAddress(rs.getString("address")); // From Passengers table
            return passenger;
        }
    }

    // Find passenger with user details by userId
    public Passenger findPassengerById(int userId) {
        String sql = """
            SELECT u.userId, u.username, u.name, u.email, u.userType, p.address 
            FROM Users u 
            LEFT JOIN Passenger p ON u.userId = p.userId 
            WHERE u.userId = ? AND u.userType = 'PASSENGER'
            """;
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), userId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Find passenger with user details by username
    public Passenger findPassengerWithUser(String username) {
        String sql = """
            SELECT u.userId, u.username, u.name, u.email, u.userType, p.address 
            FROM Users u 
            LEFT JOIN Passenger p ON u.userId = p.userId 
            WHERE u.username = ? AND u.userType = 'PASSENGER'
            """;
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), username);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Find all passengers
    public List<Passenger> findAllPassengers() {
        String sql = """
            SELECT u.userId, u.username, u.name, u.email, u.userType, p.address 
            FROM Users u 
            LEFT JOIN Passenger p ON u.userId = p.userId 
            WHERE u.userType = 'PASSENGER'
            """;
        return jdbcTemplate.query(sql, new PassengerRowMapper());
    }

    // Search passengers by address
    public List<Passenger> findByAddressContaining(String address) {
        String sql = """
            SELECT u.userId, u.username, u.name, u.email, u.userType, p.address 
            FROM Users u 
            LEFT JOIN Passenger p ON u.userId = p.userId 
            WHERE u.userType = 'PASSENGER' AND p.address LIKE ?
            """;
        return jdbcTemplate.query(sql, new PassengerRowMapper(), "%" + address + "%");
    }

    // Save passenger (assumes User already exists)
    public int savePassengerDetails(Passenger passenger) {
        // First check if passenger record exists
        String checkSql = "SELECT COUNT(*) FROM Passenger WHERE userId = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, passenger.getUserId());

        if (count > 0) {
            // Update existing passenger
            String updateSql = "UPDATE Passenger SET address = ? WHERE userId = ?";
            return jdbcTemplate.update(updateSql, passenger.getAddress(), passenger.getUserId());
        } else {
            // Insert new passenger record
            String insertSql = "INSERT INTO Passenger (userId, address) VALUES (?, ?)";
            return jdbcTemplate.update(insertSql, passenger.getUserId(), passenger.getAddress());
        }
    }

    // Update passenger details only (User details updated separately)
    public int updatePassengerDetails(Passenger passenger) {
        String sql = "UPDATE Passenger SET address = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, passenger.getAddress(), passenger.getUserId());
    }

    // Delete passenger
    public int delete(int userId) {
        String sql = "DELETE FROM Passenger WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // Count all passengers
    public int countAllPassengers() {
        String sql = "SELECT COUNT(*) FROM Users WHERE userType = 'PASSENGER'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}