package org.example.trainsystem.entity;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private int passengerId;
    private int trainId;
    private String seatNumber;
    private String bookingStatus;
    private String bookingClass;
    private LocalDate bookingDate;
    private String status;

    // Constructors, getters, and setters

    public Booking() {
    }

    public Booking(int bookingId, int passengerId, int trainId, String seatNumber, LocalDate date, String bookingStatus, String bookingClass) {
        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.trainId = trainId;
        this.seatNumber = seatNumber;
        this.bookingDate = date;
        this.bookingStatus = bookingStatus;
        this.bookingClass = bookingClass;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }


    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
