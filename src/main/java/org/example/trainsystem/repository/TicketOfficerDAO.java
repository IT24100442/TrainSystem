package org.example.trainsystem.repository;

import org.example.trainsystem.entity.TicketOfficer;
import org.example.trainsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TicketOfficerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDAO userDAO;

    private static final class TicketOfficerRowMapper implements RowMapper<TicketOfficer> {
        @Override
        public TicketOfficer mapRow(ResultSet rs, int rowNum) throws SQLException {
            TicketOfficer ticketOfficer = new TicketOfficer();
            ticketOfficer.setUserId(rs.getInt("userId"));
            ticketOfficer.setTrainId(rs.getInt("trainId"));
            return ticketOfficer;
        }
    }

    private static final class TicketOfficerWithUserRowMapper implements RowMapper<TicketOfficer> {
        @Override
        public TicketOfficer mapRow(ResultSet rs, int rowNum) throws SQLException {
            TicketOfficer ticketOfficer = new TicketOfficer();
            ticketOfficer.setUserId(rs.getInt("userId"));
            ticketOfficer.setTrainId(rs.getInt("trainId"));

            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));

            ticketOfficer.setUser(user);
            return ticketOfficer;
        }
    }

    public TicketOfficer findTicketOfficerWithUser(String username) {
        String sql = """
        SELECT t.userId, t.trainId, u.username, u.password, u.email, u.name, u.userType
        FROM TicketOfficer t
        INNER JOIN Users u ON t.userId = u.userId
        WHERE u.username = ?;
        """;
        try {
            return jdbcTemplate.queryForObject(sql, new TicketOfficerWithUserRowMapper(), username);
        } catch (Exception e) {
            return null;
        }
    }


    public int save(TicketOfficer ticketOfficer) {
        String sql = "INSERT INTO TicketOfficer (userId, trainId) VALUES (?, ?)";
        return jdbcTemplate.update(sql, ticketOfficer.getUserId(), ticketOfficer.getTrainId());
    }

    public int update(TicketOfficer ticketOfficer) {
        String sql = "UPDATE TicketOfficer SET trainId = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, ticketOfficer.getTrainId(), ticketOfficer.getUserId());
    }

    public int delete(int userId) {
        String sql = "DELETE FROM TicketOfficer WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    public TicketOfficer findById(int userId) {
        String sql = "SELECT userId, trainId FROM TicketOfficer WHERE userId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new TicketOfficerRowMapper(), userId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<TicketOfficer> findAllTicketOfficers() {
        String sql = "SELECT userId, trainId FROM TicketOfficer";
        return jdbcTemplate.query(sql, new TicketOfficerRowMapper());
    }

    public TicketOfficer findByTrainId(int trainId) {
        String sql = "SELECT userId, trainId FROM TicketOfficer WHERE trainId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new TicketOfficerRowMapper(), trainId);
        } catch (Exception e) {
            return null;
        }
    }




}
