package org.example.trainsystem.service;

import org.example.trainsystem.entity.ViolationReport;
import org.example.trainsystem.repository.ViolationReportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViolationService {

    @Autowired
    private ViolationReportDAO violationReportDAO;

    public Optional<ViolationReport> getViolationById(Integer violationId) {
        return violationReportDAO.findById(violationId);
    }

    public List<ViolationReport> getViolationsByOfficer(String officerId) {
        return violationReportDAO.findByOfficerId(officerId);
    }
    public List<ViolationReport> getPendingViolations() {
        return violationReportDAO.findByReportStatus("PENDING");
    }
    public Optional<ViolationReport> getViolationWithDetails(Integer violationId) {
        return violationReportDAO.findViolationReportWithDetails(violationId);
    }

    public boolean createViolationReport(ViolationReport violationReport) {
        return violationReportDAO.save(violationReport) > 0;
    }

    public boolean updateViolationReport(ViolationReport violationReport) {
        return violationReportDAO.update(violationReport) > 0;
    }

    public boolean deleteViolationReport(Integer violationId) {
        return violationReportDAO.delete(violationId) > 0;
    }
}
