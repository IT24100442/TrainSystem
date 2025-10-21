package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
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

    private final class PassengerRowMapper implements RowMapper<Passenger> {
        @Override
        public Passenger mapRow(ResultSet rs, int rowNum) throws SQLException {
            Passenger passenger = new Passenger();
            passenger.setUserId(rs.getInt("userId"));
            passenger.setAddress(rs.getString("passengerAddress"));

            // Map User fields if they exist in the result set
            try {
                passenger.setUsername(rs.getString("username"));
                passenger.setEmail(rs.getString("email"));
                passenger.setName(rs.getString("name"));
                passenger.setPassword(rs.getString("password"));
                passenger.setUserType(rs.getString("userType"));
            } catch (SQLException e) {
                // User fields not in result set, skip
            }

            return passenger;
        }
    }

    public int save(Passenger passenger) {
        String sql = "INSERT INTO Passenger (userId, passengerAddress) VALUES (?, ?)";
        return jdbcTemplate.update(sql,
                passenger.getUserId(),
                passenger.getAddress()
        );
    }

    // Update Passenger
    public int update(Passenger passenger) {
        String sql = "UPDATE Passenger SET passengerAddress = ? WHERE userId = ?";
        return jdbcTemplate.update(sql,
                passenger.getAddress(),
                passenger.getUserId()
        );
    }

    // Update Passenger with User fields
    public int updatePassengerWithUser(int userId, String name, String email, String address) {
        // Update User table
        String userSql = "UPDATE Users SET name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(userSql, name, email, userId);

        // Update Passenger table
        String passengerSql = "UPDATE Passenger SET passengerAddress = ? WHERE userId = ?";
        return jdbcTemplate.update(passengerSql, address, userId);
    }

    // Update password in Users table
    public int updatePassword(int userId, String encodedPassword) {
        String sql = "UPDATE Users SET password = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, encodedPassword, userId);
    }

    // Delete Passenger and associated User
    public int delete(int userId) {
        // First delete from Passenger table
        String passengerSql = "DELETE FROM Passenger WHERE userId = ?";
        jdbcTemplate.update(passengerSql, userId);

        // Then delete from Users table
        String userSql = "DELETE FROM Users WHERE userId = ?";
        return jdbcTemplate.update(userSql, userId);
    }

    // Find by ID
    public Passenger findById(int userId) {
        String sql = "SELECT * FROM Passenger WHERE userId = ?";
        return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), userId);
    }

    // Find all Passengers
    public List<Passenger> findAll() {
        String sql = "SELECT * FROM Passenger";
        return jdbcTemplate.query(sql, new PassengerRowMapper());
    }

    // Find by Username
    public Passenger findByUsername(String username) {
        String sql = "SELECT p.userId, p.passengerAddress, u.username, u.email, u.name, u.password, u.userType " +
                "FROM Passenger p " +
                "JOIN Users u ON p.userId = u.userId " +
                "WHERE u.username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), username);
        } catch (Exception e) {
            return null; // Return null if no passenger found
        }
    }

    // Find by Email
    public Passenger findByEmail(String email) {
        String sql = "SELECT p.userId, p.passengerAddress, u.username, u.email, u.name, u.password, u.userType " +
                "FROM Passenger p " +
                "JOIN Users u ON p.userId = u.userId " +
                "WHERE u.email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), email);
        } catch (Exception e) {
            return null; // Return null if no passenger found
        }
    }
}