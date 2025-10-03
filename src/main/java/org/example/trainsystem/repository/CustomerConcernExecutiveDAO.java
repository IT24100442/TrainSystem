package org.example.trainsystem.repository;

import org.example.trainsystem.entity.CustomerServiceExecutive;
import org.example.trainsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerConcernExecutiveDAO {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class CustomerConcernExecutiveRowMapper implements RowMapper<CustomerServiceExecutive> {
        @Override
        public CustomerServiceExecutive mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerServiceExecutive executive = new CustomerServiceExecutive();
            executive.setUserId(rs.getInt("userId"));
            executive.setContactNum(rs.getString("contactNum"));


            return executive;
        }
    }

    private static final class CustomerConcernExecWithUserRowMapper implements RowMapper<CustomerServiceExecutive> {
        @Override
        public CustomerServiceExecutive mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerServiceExecutive executive = new CustomerServiceExecutive();
            executive.setUserId(rs.getInt("userId"));
            executive.setContactNum(rs.getString("contactNum"));

            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));


            executive.setUser(user);
            return executive;
        }
    }

    public CustomerServiceExecutive findById(int userId) {
        String sql = "SELECT * FROM CustomerServiceExecutive WHERE userId = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new CustomerConcernExecutiveRowMapper());
    }

    public int save(CustomerServiceExecutive executive) {
        String sql = "INSERT INTO CustomerServiceExecutive (userId, contactNum) VALUES (?, ?)";
        return jdbcTemplate.update(sql, executive.getUserId(), executive.getContactNum());
    }

    public int update(CustomerServiceExecutive executive) {
        String sql = "UPDATE CustomerServiceExecutive SET contactNum = ? WHERE userId = ?";
        return jdbcTemplate.update(sql, executive.getContactNum(), executive.getUserId());
    }

    public int delete(int userId) {
        String sql = "DELETE FROM CustomerServiceExecutive WHERE userId = ?";
        return jdbcTemplate.update(sql, userId);
    }

    public CustomerServiceExecutive findCustomerExecutiveWithUser(String username) {
        String sql = """
        SELECT c.userId, c.contactNum, u.username, u.password, u.email, u.name, u.userType
        FROM CustomerServiceExecutive c
        INNER JOIN Users u ON c.userId = u.userId
        WHERE u.username = ?;
        """;
        try {
            return jdbcTemplate.queryForObject(sql, new CustomerConcernExecWithUserRowMapper(), username);
        } catch (Exception e) {
            return null;
        }
    }

    public List <CustomerServiceExecutive> findAll() {
        String sql = "SELECT * FROM CustomerServiceExecutive";
        return jdbcTemplate.query(sql, new CustomerConcernExecutiveRowMapper());
    }

}
