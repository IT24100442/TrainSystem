package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import  org.example.trainsystem.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getString("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));
            return user;
        }
    }

    public User findById(String userId) {
        String sql = "SELECT userId, username, password, email, name, userType FROM Users WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT userId, username, userType, password, email, name FROM Users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            System.out.println("No user found with username: " + username);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<User> findByUserType(String userType) {
        String sql = "SELECT userId, username, password, email, name, userType FROM Users WHERE userType = ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), userType);
    }

    public int save(User user) {
        String sql = "INSERT INTO Users (userId, username, password, email, name, userType) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUserId(), user.getUsername(), user.getPassword(),
                user.getEmail(), user.getName(), user.getUserType());
    }

    public int update(User user) {
        String sql = "UPDATE Users SET username = ?, password = ?, email = ?, name = ?, userType = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail(),
                user.getName(), user.getUserType(), user.getUserId());
    }

    public int delete(String userId) {
        String sql = "DELETE FROM Users WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }
}