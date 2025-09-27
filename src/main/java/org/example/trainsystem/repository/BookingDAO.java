package org.example.trainsystem.repository;


import org.example.trainsystem.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            Booking booking = new Booking();
            booking.setBookingId(rs.getInt("bookingID"));
            booking.setPassengerId(rs.getInt("passengerId"));
            booking.setTrainId(rs.getInt("trainId"));
            booking.setBookingStatus(rs.getString("bookingStatus"));
            booking.setSeatNumber(rs.getString("seat"));
            booking.setBookingClass(rs.getString("class"));


            Timestamp timestamp = rs.getTimestamp("bookingDate");
            if (timestamp != null) {
                booking.setBookingDate(LocalDate.from(timestamp.toLocalDateTime()));
            }

            return booking;
        }


    }

    public int getLatestBookingId() {
        String sql = "SELECT TOP 1 bookingId FROM Booking ORDER BY bookingId DESC";
        Integer maxId = jdbcTemplate.queryForObject(sql, Integer.class);
        return (maxId != null) ? maxId : 0;
    }


    public Booking findById(int id) {
        String sql = "SELECT * FROM Booking WHERE bookingId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BookingRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }
    public int save(Booking booking) {
        String sql = "INSERT INTO Booking(passengerId, trainId, bookingStatus, class, bookingDate,seat) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, booking.getPassengerId(), booking.getTrainId(), booking.getBookingStatus(), booking.getBookingClass(), Timestamp.valueOf(booking.getBookingDate().atStartOfDay()),booking.getSeatNumber());
    }
    public int update(Booking booking) {
        String sql = "UPDATE Booking SET passengerId = ?, trainId = ?, bookingStatus = ?, class = ?, bookingDate = ?, seat = ? WHERE bookingId = ?";
        return jdbcTemplate.update(sql, booking.getPassengerId(), booking.getTrainId(), booking.getBookingStatus(), booking.getBookingClass(), Timestamp.valueOf(booking.getBookingDate().atStartOfDay()),booking.getSeatNumber(), booking.getBookingId());
    }
    public int delete(int id) {
        String sql = "DELETE FROM Booking WHERE bookingId = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Booking> findAll() {
        String sql = "SELECT * FROM Booking ORDER BY bookingId";
        return jdbcTemplate.query(sql, new BookingRowMapper());
    }

    public List<Booking> findByPassengerId(int passengerId) {
        String sql = "SELECT * FROM Booking WHERE passengerId = ?";
        return jdbcTemplate.query(sql, new BookingRowMapper(), passengerId);

    }
    public List<Booking> findByTrainId(int trainId) {
        String sql = "SELECT * FROM Booking WHERE trainId = ?";
        return jdbcTemplate.query(sql, new BookingRowMapper(), trainId);
    }
}
