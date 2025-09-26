package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Payment;
import org.example.trainsystem.entity.StatusUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PaymentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class PaymentRowMapper implements RowMapper<Payment> {
        @Override
        public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Payment payment = new Payment();
            payment.setPaymentId(rs.getInt("paymentId"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setBookingId(rs.getInt("bookingId"));


            Timestamp timestamp = rs.getTimestamp("updateTime");
            if (timestamp != null) {
                payment.setPaymentDate(timestamp.toLocalDateTime());
            }

            return payment;
        }
    }
    public Payment findById(int id) {
        String sql = "SELECT id, bookingId, amount, paymentDate FROM Payment WHERE paymentId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new PaymentRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM Payment";
        return jdbcTemplate.query(sql, new PaymentRowMapper());
    }

    public int save(Payment payment) {
        String sql = "INSERT INTO Payment(bookingId, amount, paymentDate) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, payment.getBookingId(), payment.getAmount(), Timestamp.valueOf(payment.getPaymentDate()));
    }

    public int update(Payment payment) {
        String sql = "UPDATE Payment SET bookingId = ?, amount = ?, paymentDate = ? WHERE paymentId = ?";
        return jdbcTemplate.update(sql, payment.getBookingId(), payment.getAmount(), Timestamp.valueOf(payment.getPaymentDate()), payment.getPaymentId());
    }

    public int delete(Payment payment) {
        String sql = "DELETE FROM Payment WHERE paymentId = ?";
        return jdbcTemplate.update(sql, payment.getPaymentId());
    }

}
