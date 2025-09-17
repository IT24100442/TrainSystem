package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class MessageDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class MessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            Message message = new Message();
            message.setMsgId(rs.getInt("msgId"));
            message.setSenderId(rs.getInt("senderId"));
            message.setReceiverId(rs.getInt("receiverId"));
            message.setMsgText(rs.getString("msgText"));

            Timestamp timestamp = rs.getTimestamp("msgTime");
            if (timestamp != null) {
                message.setMsgTime(timestamp.toLocalDateTime());
            }

            return message;
        }
    }

    public Message findById(Integer msgId) {
        String sql = "SELECT msgId, senderId, receiverId, msgText, msgTime FROM Msgs WHERE msgId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new MessageRowMapper(), msgId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Message> findRecentMessagesByReceiverId(int receiverId, int limit) {
        String sql = "SELECT msgId, senderId, receiverId, msgText, msgTime " +
                "FROM Msgs " +
                "WHERE receiverId = ? " +
                "ORDER BY msgTime DESC " +
                "OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, new MessageRowMapper(), receiverId, limit);
    }

    public List<Message> findByReceiverId(int receiverId) {
        String sql = "SELECT msgId, senderId, receiverId, msgText, msgTime FROM Msgs WHERE receiverId = ? ORDER BY msgTime DESC";
        return jdbcTemplate.query(sql, new MessageRowMapper(), receiverId);
    }

    public List<Message> findBySenderId(int senderId) {
        String sql = "SELECT msgId, senderId, receiverId, msgText, msgTime FROM Msgs WHERE senderId = ? ORDER BY msgTime DESC";
        return jdbcTemplate.query(sql, new MessageRowMapper(), senderId);
    }

    // Save message without specifying msgTime; database default GETDATE() will be used
    public int save(Message message) {
        String sql = "INSERT INTO Msgs (senderId, receiverId, msgText) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, message.getSenderId(), message.getReceiverId(),
                message.getMsgText());
    }

    public int delete(int msgId) {
        String sql = "DELETE FROM Msgs WHERE msgId = ?";
        return jdbcTemplate.update(sql, msgId);
    }
}
