package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.User;

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
            user.setUserId(rs.getInt("userId"));   // now int instead of String
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));
            return user;
        }
    }

    public User findById(int userId) {
        String sql = "SELECT userId, username, password, email, name, userType FROM Users WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT userId, username, password, email, name, userType FROM Users WHERE username = ?";
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

    // ✅ Fixed: save() now retrieves the auto-generated key
    public int save(User user) {
        String sql = "INSERT INTO Users (username, password, email, name, userType) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getName());
            ps.setString(5, user.getUserType());
            return ps;
        }, keyHolder);

        int generatedId = keyHolder.getKey().intValue();
        user.setUserId(generatedId); // update entity with DB id
        return generatedId;
    }

    public int update(User user) {
        String sql = "UPDATE Users SET username = ?, password = ?, email = ?, name = ?, userType = ? WHERE userId = ?";
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                user.getUserType(),
                user.getUserId());
    }

    public int delete(int userId) {
        String sql = "DELETE FROM Users WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

}
