package org.example.trainsystem.service;

import org.example.trainsystem.entity.StatusUpdate;
import org.example.trainsystem.repository.StatusUpdateDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatusUpdateService {

    @Autowired
    private StatusUpdateDAO statusUpdateDAO;

    /**
     * Save a new location update
     */
    public StatusUpdate save(StatusUpdate statusUpdate) {
        if (statusUpdate.getUpdateTime() == null) {
            statusUpdate.setUpdateTime(LocalDateTime.now());
        }
        int id = StatusUpdateDAO.save(statusUpdate);
        statusUpdate.setId((long) id);
        return statusUpdate;
    }

    /**
     * Update an existing status update
     */
    public int update(StatusUpdate statusUpdate) {
        if (statusUpdate.getUpdateTime() == null) {
            statusUpdate.setUpdateTime(LocalDateTime.now());
        }
        return statusUpdateDAO.update(statusUpdate);
    }

    /**
     * Delete a status update by ID
     */
    public int delete(Long id) {
        return statusUpdateDAO.delete(id);
    }

    /**
     * Get the latest status update for a given train
     */
    public StatusUpdate getLatestByTrainId(int trainId) {
        return StatusUpdateDAO.findLatestByTrainId(trainId);
    }

    /**
     * Get all status updates for a given train
     */
    public List<StatusUpdate> getByTrainId(int trainId) {
        return statusUpdateDAO.findByTrainId(trainId);
    }

    /**
     * Get a status update by ID
     */
    public StatusUpdate getById(Long id) {
        return statusUpdateDAO.findById(id);
    }
}
