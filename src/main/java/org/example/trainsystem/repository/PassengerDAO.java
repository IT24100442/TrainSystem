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
            passenger.setAddress(rs.getString("passengerAddress"));

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

    // Delete Passenger
    public int delete(int userId) {
        String sql = "DELETE FROM Passenger WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
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
