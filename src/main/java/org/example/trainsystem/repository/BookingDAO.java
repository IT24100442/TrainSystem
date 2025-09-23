package org.example.trainsystem.repository;

import org.example.trainsystem.entity.Booking;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingDAO {
    public List<Booking> findByTrainId(String trainId) {
    return null;}

    public Booking findById(String bookingId) {
        return null;
    }

    public int update(Booking booking) {
        return 0;
    }
}
