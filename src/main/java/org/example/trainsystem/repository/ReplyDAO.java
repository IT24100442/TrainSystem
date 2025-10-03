package org.example.trainsystem.repository;


import org.example.trainsystem.entity.Concern;
import org.example.trainsystem.entity.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ReplyDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class ConcernRowMapper implements RowMapper<Reply> {
        @Override
        public Reply mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reply reply = new Reply();
            reply.setReplyText(rs.getString("replyText"));
            reply.setConcernId(rs.getInt("concernId"));
            reply.setReplyId(rs.getInt("replyId"));
            reply.setExecId(rs.getInt("execId"));

            Timestamp timestamp = rs.getTimestamp("replyTime");
            if (timestamp != null) {
                reply.setReplyTime(timestamp.toLocalDateTime());
            }

            return reply;
        }
    }

    private static final class ReplyWithConcernRowMapper implements RowMapper<Reply> {
        @Override
        public Reply mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reply reply = new Reply();
            reply.setReplyText(rs.getString("replyText"));
            reply.setConcernId(rs.getInt("concernId"));
            reply.setReplyId(rs.getInt("replyId"));
            reply.setExecId(rs.getInt("execId"));

            Timestamp timestamp = rs.getTimestamp("replyTime");
            if (timestamp != null) {
                reply.setReplyTime(timestamp.toLocalDateTime());
            }

            Concern concern = new Concern();
            concern.setConcernId(rs.getInt("concernId"));
            concern.setPassengerId(rs.getInt("passengerId"));
            concern.setStatus(rs.getString("status"));
            concern.setDescription(rs.getString("description"));



            Timestamp concernTimestamp = rs.getTimestamp("date_submitted");
            if (concernTimestamp != null) {
                concern.setDate_submitted(concernTimestamp.toLocalDateTime());
            }

            reply.setConcern(concern);
            return reply;
        }
    }

    public int save(Reply reply) {
        String sql = "INSERT INTO Reply (replyText, concernId, replyTime, execId) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, reply.getReplyText(), reply.getConcernId(), Timestamp.valueOf(reply.getReplyTime()), reply.getExecId());
    }

    public Reply findByConcernId(int concernId) {
        String sql = "SELECT r.*, c.description, c.status, c.passengerId, c.date_submitted FROM Reply r JOIN Concern c ON r.concernId = c.concernId WHERE r.concernId = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{concernId}, new ReplyWithConcernRowMapper());
    }

    public int delete (int replyId) {
        String sql = "DELETE FROM Reply WHERE replyId = ?";
        return jdbcTemplate.update(sql, replyId);
    }

    public List<Reply> findAll() {
        String sql = "SELECT * FROM Reply";
        return jdbcTemplate.query(sql, new ConcernRowMapper());
    }

    public List<Reply> findByPassengerId(int passengerId) {
        String sql = "SELECT r.*, c.description, c.status, c.passengerId, c.date_submitted FROM Reply r JOIN Concern c ON r.concernId = c.concernId WHERE c.passengerId = ?";
        return jdbcTemplate.query(sql, new ReplyWithConcernRowMapper(), passengerId);
    }

//    public List<Reply> findByPassengerId(int passengerId) {
//
//        try{
//            String sql = "SELECT TOP 1 r.replyId, r.concernId, r.execId, r.replyText, r.replyTime FROM Reply r JOIN Concern c ON r.concernId = c.concernId WHERE c.passengerId = ? ORDER BY r.replyId DESC";
//            return jdbcTemplate.query(sql, new ReplyWithConcernRowMapper(), passengerId);
//        }
//        catch (Exception e){
//            return null;
//        }
//
//    }


}
