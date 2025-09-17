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
import java.util.List;
import java.util.Optional;

@Repository
public class ViolationReportDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ---------- RowMappers ----------
    private static final class ViolationReportRowMapper implements RowMapper<ViolationReport> {
        @Override
        public ViolationReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViolationReport violationReport = new ViolationReport();
            violationReport.setViolationId(rs.getInt("violationId"));
            violationReport.setOfficerId(rs.getString("officerId"));
            violationReport.setTrainId(rs.getString("trainId"));
            violationReport.setPassengerId(rs.getString("passengerId"));
            violationReport.setViolationType(rs.getString("violationType"));
            violationReport.setViolationDescription(rs.getString("violationDescription"));
            violationReport.setViolationTime(rs.getTimestamp("violationTime"));
            violationReport.setReportStatus(rs.getString("reportStatus"));
            violationReport.setPenaltyAmount(rs.getBigDecimal("penaltyAmount"));
            violationReport.setResolvedBy(rs.getString("resolvedBy"));
            violationReport.setResolutionTime(rs.getTimestamp("resolutionTime"));
            return violationReport;
        }
    }

    private static final class ViolationReportWithDetailsRowMapper implements RowMapper<ViolationReport> {
        @Override
        public ViolationReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ViolationReport violationReport = new ViolationReport();
            violationReport.setViolationId(rs.getInt("violationId"));
            violationReport.setOfficerId(rs.getString("officerId"));
            violationReport.setTrainId(rs.getString("trainId"));
            violationReport.setPassengerId(rs.getString("passengerId"));
            violationReport.setViolationType(rs.getString("violationType"));
            violationReport.setViolationDescription(rs.getString("violationDescription"));
            violationReport.setViolationTime(rs.getTimestamp("violationTime"));
            violationReport.setReportStatus(rs.getString("reportStatus"));
            violationReport.setPenaltyAmount(rs.getBigDecimal("penaltyAmount"));
            violationReport.setResolvedBy(rs.getString("resolvedBy"));
            violationReport.setResolutionTime(rs.getTimestamp("resolutionTime"));

            TicketOfficer ticketOfficer = new TicketOfficer();
            ticketOfficer.setUserId(rs.getString("officerId"));
            ticketOfficer.setBadgeNumber(rs.getString("badgeNumber"));
            ticketOfficer.setAssignedRoute(rs.getString("assignedRoute"));

            User user = new User();
            user.setUserId(rs.getInt("officerId"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setUserType(rs.getString("userType"));
            // 🚨 Password excluded for security reasons

            ticketOfficer.setUser(user);
            violationReport.setTicketOfficer(ticketOfficer);

            return violationReport;
        }
    }

    // ---------- Queries ----------
    public Optional<ViolationReport> findById(Integer violationId) {
        String sql = """
            SELECT violationId, officerId, trainId, passengerId, violationType,
                   violationDescription, violationTime, reportStatus, penaltyAmount,
                   resolvedBy, resolutionTime
            FROM ViolationReport
            WHERE violationId = ?
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
            SELECT violationId, officerId, trainId, passengerId, violationType,
                   violationDescription, violationTime, reportStatus, penaltyAmount,
                   resolvedBy, resolutionTime
            FROM ViolationReport
            WHERE officerId = ?
        """;
        return jdbcTemplate.query(sql, new ViolationReportRowMapper(), officerId);
    }

    public Optional<ViolationReport> findViolationReportWithDetails(Integer violationId) {
        String sql = """
            SELECT vr.violationId, vr.officerId, vr.trainId, vr.passengerId, vr.violationType,
                   vr.violationDescription, vr.violationTime, vr.reportStatus, vr.penaltyAmount,
                   vr.resolvedBy, vr.resolutionTime,
                   t.badgeNumber, t.assignedRoute,
                   u.username, u.email, u.name, u.userType
            FROM ViolationReport vr
            INNER JOIN TicketOfficer t ON vr.officerId = t.userId
            INNER JOIN Users u ON t.userId = u.userId
            WHERE vr.violationId = ?
        """;
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new ViolationReportWithDetailsRowMapper(), violationId)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(ViolationReport violationReport) {
        String sql = """
            INSERT INTO ViolationReport
                (officerId, trainId, passengerId, violationType, violationDescription,
                 violationTime, reportStatus, penaltyAmount, resolvedBy, resolutionTime)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return jdbcTemplate.update(sql,
                violationReport.getOfficerId(),
                violationReport.getTrainId(),
                violationReport.getPassengerId(),
                violationReport.getViolationType(),
                violationReport.getViolationDescription(),
                violationReport.getViolationTime(),
                violationReport.getReportStatus(),
                violationReport.getPenaltyAmount(),
                violationReport.getResolvedBy(),
                violationReport.getResolutionTime()
        );
    }

    public int update(ViolationReport violationReport) {
        String sql = """
            UPDATE ViolationReport
            SET officerId = ?, trainId = ?, passengerId = ?, violationType = ?,
                violationDescription = ?, violationTime = ?, reportStatus = ?,
                penaltyAmount = ?, resolvedBy = ?, resolutionTime = ?
            WHERE violationId = ?
        """;
        return jdbcTemplate.update(sql,
                violationReport.getOfficerId(),
                violationReport.getTrainId(),
                violationReport.getPassengerId(),
                violationReport.getViolationType(),
                violationReport.getViolationDescription(),
                violationReport.getViolationTime(),
                violationReport.getReportStatus(),
                violationReport.getPenaltyAmount(),
                violationReport.getResolvedBy(),
                violationReport.getResolutionTime(),
                violationReport.getViolationId()
        );
    }

    public int delete(Integer violationId) {
        String sql = "DELETE FROM ViolationReport WHERE violationId = ?";
        return jdbcTemplate.update(sql, violationId);
    }
}
