package com.codegym.service;

import com.codegym.model.HireSession;
import com.codegym.model.Report;
import com.codegym.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public Report saveReport(HireSession session, String content) {

        Report report = new Report();
        report.setHireSession(session);
        report.setCcdv(session.getCcdv());
        report.setUser(session.getUser());
        report.setReportContent(content);
        report.setCreatedAt(LocalDateTime.now());

        return reportRepository.save(report);
    }

    public java.util.List<Report> findAll() {
        return reportRepository.findAll();
    }

}
