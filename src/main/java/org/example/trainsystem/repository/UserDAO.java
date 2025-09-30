package org.example.trainsystem.repository;

import org.example.trainsystem.entity.User;
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
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setUserType(rs.getString("userType"));
            return user;
        }
    }

    // Find user by username (needed for CustomUserDetailsService)
    public User findByUsername(String username) {
        String sql = "SELECT userId, username, password, name, email, userType FROM Users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Find user by userId
    public User findById(int userId) {
        String sql = "SELECT userId, username, password, name, email, userType FROM Users WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Save new user and return generated userId
    public int saveUser(User user) {
        String sql = "INSERT INTO Users (username, password, name, email, userType) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); // Should be BCrypt encoded
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getUserType());
            return ps;
        }, keyHolder);

        int generatedUserId = keyHolder.getKey().intValue();
        user.setUserId(generatedUserId); // Set the generated ID back on the entity
        return generatedUserId;
}

    // Update user details
    public int updateUser(User user) {
        String sql = "UPDATE Users SET name = ?, email = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getUserId());
    }

    // Find users by userType
    public List<User> findByUserType(String userType) {
        String sql = "SELECT userId, username, password, name, email, userType FROM Users WHERE userType = ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), userType);
    }

    // Find all users
    public List<User> findAll() {
        String sql = "SELECT userId, username, password, name, email, userType FROM Users ORDER BY userId";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    // Delete user by userId
    public int delete(int userId) {
        String sql = "DELETE FROM Users WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // Check if username exists
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;
    }
}