package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            passenger.setAddress(rs.getString("address"));
            passenger.setUserType(rs.getString("userType"));
            return passenger;
        }
    }

    // Find passenger by userId
    public Passenger findPassengerById(int userId) {
        String sql = "SELECT userId, username, name, email, address, userType FROM Passengers WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), userId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Find passenger by username
    public Passenger findPassengerWithUser(String username) {
        String sql = "SELECT userId, username, name, email, address, userType FROM Passengers WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), username);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Find all passengers
    public List<Passenger> findAllPassengers() {
        String sql = "SELECT userId, username, name, email, address, userType FROM Passengers";
        return jdbcTemplate.query(sql, new PassengerRowMapper());
    }

    // Search passengers by address
    public List<Passenger> findByAddressContaining(String address) {
        String sql = "SELECT userId, username, name, email, address, userType FROM Passengers WHERE address LIKE ?";
        return jdbcTemplate.query(sql, new PassengerRowMapper(), "%" + address + "%");
    }

    // Save passenger (auto-generate key if needed)
    public int save(Passenger passenger) {
        String sql = "INSERT INTO Passengers (userId, username, name, email, address, userType) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, passenger.getUserId()); // from registered User
            ps.setString(2, passenger.getUsername());
            ps.setString(3, passenger.getName());
            ps.setString(4, passenger.getEmail());
            ps.setString(5, passenger.getAddress());
            ps.setString(6, passenger.getUserType());
            return ps;
        }, keyHolder);

        return passenger.getUserId();
    }

    // Update passenger
    public int update(Passenger passenger) {
        String sql = "UPDATE passengers SET name=?, email=?, address=? WHERE user_id=?";
        return jdbcTemplate.update(sql,
                passenger.getName(),
                passenger.getEmail(),
                passenger.getAddress(),
                passenger.getUserId()
        );
    }


    // Delete passenger
    public int delete(int userId) {
        String sql = "DELETE FROM Passengers WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // Count all passengers
    public int countAllPassengers() {
        String sql = "SELECT COUNT(*) FROM Passengers";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
