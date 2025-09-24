package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Passenger;
import org.example.trainsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PassengerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // -----------------------------
    // RowMappers
    // -----------------------------
    private static final class PassengerRowMapper implements RowMapper<Passenger> {
        @Override
        public Passenger mapRow(ResultSet rs, int rowNum) throws SQLException {
            Passenger passenger = new Passenger();
            passenger.setUserId(rs.getInt("userId"));
            passenger.setAddress(rs.getString("address"));
            return passenger;
        }
    }

    private static final class PassengerWithUserRowMapper implements RowMapper<Passenger> {
        @Override
        public Passenger mapRow(ResultSet rs, int rowNum) throws SQLException {
            Passenger passenger = new Passenger();
            passenger.setUserId(rs.getInt("userId"));
            passenger.setUsername(rs.getString("username"));
            passenger.setPassword(rs.getString("password"));
            passenger.setEmail(rs.getString("email"));
            passenger.setName(rs.getString("name"));
            passenger.setUserType(rs.getString("userType"));
            passenger.setAddress(rs.getString("address"));
            return passenger;
        }
    }

    // -----------------------------
    // CRUD Operations
    // -----------------------------

    // Find passenger by userId
    public Passenger findPassengerById(int userId) {
        String sql = "SELECT userId, address FROM Passenger WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    // Find passenger with user info by username
    public Passenger findPassengerWithUser(String username) {
        String sql = """
                SELECT p.userId, p.address, u.username, u.password, u.email, u.name, u.userType
                FROM Passenger p
                INNER JOIN Users u ON p.userId = u.userId
                WHERE u.username = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerWithUserRowMapper(), username);
        } catch (Exception e) {
            return null;
        }
    }

    // Insert a passenger (user must exist)
    public int save(Passenger passenger) {
        String sql = "INSERT INTO Passenger (userId, address) VALUES (?, ?)";
        return jdbcTemplate.update(sql, passenger.getUserId(), passenger.getAddress());
    }

    // Update passenger
    public int update(Passenger passenger) {
        String sql = "UPDATE Passenger SET address = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, passenger.getAddress(), passenger.getUserId());
    }

    // Delete passenger by userId
    public int delete(int userId) {
        String sql = "DELETE FROM Passenger WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // Find all passengers
    public List<Passenger> findAllPassengers() {
        String sql = "SELECT userId, address FROM Passenger";
        return jdbcTemplate.query(sql, new PassengerRowMapper());
    }

    // Search passengers by address
    public List<Passenger> findByAddressContaining(String address) {
        String sql = "SELECT userId, address FROM Passenger WHERE address LIKE ?";
        return jdbcTemplate.query(sql, new PassengerRowMapper(), "%" + address + "%");
    }

    // Count all passengers
    public int countAllPassengers() {
        String sql = "SELECT COUNT(*) FROM Passenger";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    // -----------------------------
    // Transactional method: Insert User and Passenger together
    // -----------------------------
    @Transactional
    public Passenger saveUserAndPassenger(Passenger passenger) {
        // 1️⃣ Insert into Users and get generated userId
        String sqlUser = "INSERT INTO Users (username, password, email, name, userType) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlUser, new String[]{"userId"});
            ps.setString(1, passenger.getUsername());
            ps.setString(2, passenger.getPassword());
            ps.setString(3, passenger.getEmail());
            ps.setString(4, passenger.getName());
            ps.setString(5, "passenger");
            return ps;
        }, keyHolder);

        int generatedUserId = keyHolder.getKey().intValue();
        passenger.setUserId(generatedUserId);

        // 2️⃣ Insert into Passenger
        save(passenger);

        return passenger;
    }
}
