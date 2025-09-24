package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PassengerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Simple RowMapper for Passenger only (like DriverRowMapper)
    private static final class PassengerRowMapper implements RowMapper<Passenger> {
        @Override
        public Passenger mapRow(ResultSet rs, int rowNum) throws SQLException {
            Passenger passenger = new Passenger();
            passenger.setUserId(Integer.parseInt(rs.getString("userId")));
            passenger.setAddress(rs.getString("address"));
            return passenger;
        }
    }

    private static final class PassengerWithUserRowMapper implements RowMapper<Passenger> {
        @Override
        public Passenger mapRow(ResultSet rs, int rowNum) throws SQLException {
            Passenger passenger = new Passenger();
            passenger.setUserId(rs.getInt("userId"));   // use int if userId is numeric
            passenger.setUsername(rs.getString("username"));
            passenger.setPassword(rs.getString("password"));
            passenger.setEmail(rs.getString("email"));
            passenger.setName(rs.getString("name"));
            passenger.setUserType("passenger"); // discriminator
            passenger.setAddress(rs.getString("address"));
            return passenger;
        }
    }


    // Find passenger by ID (like findById in DriverDAO)
    public Passenger findById(String userId) {
        String sql = "SELECT userId, address FROM Passenger WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    // Find passenger with user data (exactly like findDriverWithUser)
    public Passenger findPassengerWithUser(String username) {
        String sql = """
        SELECT p.userId, p.address, u.username, u.password, u.email, u.name
        FROM Passenger p
        INNER JOIN Users u ON p.userId = u.userId
        WHERE u.username = ?;
        """;
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerWithUserRowMapper(), username);
        } catch (Exception e) {
            return null;
        }
    }

    // Save new passenger (like save in DriverDAO)
    public int save(Passenger passenger) {
        String sql = "INSERT INTO Passenger (userId, address) VALUES (?, ?)";
        return jdbcTemplate.update(sql, passenger.getUserId(), passenger.getAddress());
    }

    // Update passenger (like update in DriverDAO)
    public int update(Passenger passenger) {
        String sql = "UPDATE Passenger SET address = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, passenger.getAddress(), passenger.getUserId());
    }

    // Delete passenger (like delete in DriverDAO)
    public int delete(String userId) {
        String sql = "DELETE FROM Passenger WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // Find all passengers (like findAllDrivers)
    public List<Passenger> findAllPassengers() {
        String sql = "SELECT userId, address FROM Passenger";
        return jdbcTemplate.query(sql, new PassengerRowMapper());
    }

    // Find passenger by ID (alternative method like findDriverById)
    public Passenger findPassengerById(String userId) {
        String sql = "SELECT userId, address FROM Passenger WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), userId);
        } catch (Exception e) {
            return null; // Return null if not found
        }
    }

    // Search passengers by address (additional method for passenger-specific search)
    public List<Passenger> findByAddressContaining(String address) {
        String sql = "SELECT userId, address FROM Passenger WHERE address LIKE ?";
        return jdbcTemplate.query(sql, new PassengerRowMapper(), "%" + address + "%");
    }

    // Get passenger count (additional utility method)
    public int countAllPassengers() {
        String sql = "SELECT COUNT(*) FROM Passenger";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }
}