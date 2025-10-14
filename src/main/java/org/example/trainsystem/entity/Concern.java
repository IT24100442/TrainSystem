package org.example.trainsystem.entity;

import java.time.LocalDateTime;

public class Concern {
    private int concernId;
    private int passengerId;
    private LocalDateTime date_submitted;
    private String status;
    private String description;

    public Concern() {
    }

    public Concern(int concernId, int passengerId, LocalDateTime date_submitted, String status, String description) {
        this.concernId = concernId;
        this.passengerId = passengerId;
        this.date_submitted = date_submitted;
        this.status = status;
        this.description = description;
    }

    public int getConcernId() {
        return concernId;
    }

    public void setConcernId(int concernId) {
        this.concernId = concernId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public LocalDateTime getDate_submitted() {
        return date_submitted;
    }

    public void setDate_submitted(LocalDateTime date_submitted) {
        this.date_submitted = date_submitted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
