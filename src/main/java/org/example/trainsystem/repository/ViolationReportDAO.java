package org.example.trainsystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.example.trainsystem.entity.ViolationReport;
import org.example.trainsystem.entity.TicketOfficer;
import org.example.trainsystem.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ViolationReportDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ViolationReport> findByReportStatus(String status) {
        {
            String sql = """
        SELECT violationId, officerId, trainId, passengerId, violationType,
               violationDescription, violationTime, reportStatus, penaltyAmount,
               resolvedBy, resolutionTime
        FROM ViolationReport
        WHERE reportStatus = ?
    """;
            return jdbcTemplate.query(sql, new ViolationReportRowMapper(), status);
        }
    }


    // ---------- RowMappers ----------
    private static final class ViolationReportRowMapper implements RowMapper<ViolationReport> {
        @Override
        public ViolationReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViolationReport violationReport = new ViolationReport();
            violationReport.setReportId(rs.getInt("reportId"));
            violationReport.setTicketOfficerId(rs.getInt("ticketOfficerId"));
            violationReport.setTrainId(rs.getInt("trainId"));
            violationReport.setPassengerId(rs.getInt("passengerId"));
            violationReport.setViolationType(rs.getString("violationType"));

            Timestamp timestamp = rs.getTimestamp("reportDate");
            if (timestamp != null) {
                violationReport.setReportDate(timestamp.toLocalDateTime());
            }

            return violationReport;
        }
    }

//    private static final class ViolationReportWithDetailsRowMapper implements RowMapper<ViolationReport> {
//        @Override
//        public ViolationReport mapRow(ResultSet rs, int rowNum) throws SQLException {
//            ViolationReport violationReport = new ViolationReport();
//            violationReport.se(rs.getInt("reportId"));
//            violationReport.setOfficerId(rs.getString("ticketOfficerId"));
//            violationReport.setTrainId(rs.getString("trainId"));
//            violationReport.setPassengerId(rs.getString("passengerId"));
//            violationReport.setViolationType(rs.getString("violationType"));
//            violationReport.setViolationTime(rs.getTimestamp("reportDate"));
//
//            TicketOfficer ticketOfficer = new TicketOfficer();
//            ticketOfficer.setUserId(rs.getString("userId"));
//
//            User user = new User();
//            user.setUserId(rs.getInt("userId"));
//            user.setUsername(rs.getString("username"));
//            user.setEmail(rs.getString("email"));
//            user.setName(rs.getString("name"));
//            user.setUserType(rs.getString("userType"));
//            // 🚨 Password excluded for security reasons
//
//            ticketOfficer.setUser(user);
//            violationReport.setTicketOfficer(ticketOfficer);
//
//            return violationReport;
//        }
//    }

    // ---------- Queries ----------
    public Optional<ViolationReport> findById(Integer violationId) {
        String sql = """
            SELECT *
            FROM ViolationReport
            WHERE reportId = ?
        """;
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new ViolationReportRowMapper(), violationId)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<ViolationReport> findByOfficerId(String officerId) {
        String sql = """
            SELECT *
            FROM ViolationReport
            WHERE ticketOfficerId = ?
        """;
        return jdbcTemplate.query(sql, new ViolationReportRowMapper(), officerId);
    }

//    public Optional<ViolationReport> findViolationReportWithDetails(Integer violationId) {
//        String sql = """
//            SELECT vr.violationId, vr.officerId, vr.trainId, vr.passengerId, vr.violationType,
//                   vr.violationDescription, vr.violationTime, vr.reportStatus, vr.penaltyAmount,
//                   vr.resolvedBy, vr.resolutionTime
//                    , t.assignedRoute,
//                   u.username, u.email, u.name, u.userType
//            FROM ViolationReport vr
//            INNER JOIN TicketOfficer t ON vr.officerId = t.userId
//            INNER JOIN Users u ON t.userId = u.userId
//            WHERE vr.violationId = ?
//        """;
//        try {
//            return Optional.ofNullable(
//                    jdbcTemplate.queryForObject(sql, new ViolationReportWithDetailsRowMapper(), violationId)
//            );
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        }
//    }

    public int save(ViolationReport violationReport) {
        String sql = """
            INSERT INTO ViolationReport
                (reportId, trainId, passengerId, violationType,
                 reportDate)
            VALUES (?, ?, ?, ?, ?)
        """;
        return jdbcTemplate.update(sql,
                violationReport.getReportId(),
                violationReport.getTrainId(),
                violationReport.getPassengerId(),
                violationReport.getViolationType(),
                violationReport.getReportDate()

        );
    }

    public int update(ViolationReport violationReport) {
        String sql = """
            UPDATE ViolationReport
            SET ticketOfficerId = ?, trainId = ?, passengerId = ?, violationType = ?,
                 reportDate = ?
            WHERE reportId = ?
        """;
        return jdbcTemplate.update(sql,
                violationReport.getTicketOfficerId(),
                violationReport.getTrainId(),
                violationReport.getPassengerId(),
                violationReport.getViolationType(),
                violationReport.getReportDate(),
                violationReport.getReportId()
        );
    }

    public int delete(Integer violationId) {
        String sql = "DELETE FROM ViolationReport WHERE reportId = ?";
        return jdbcTemplate.update(sql, violationId);
    }
}
