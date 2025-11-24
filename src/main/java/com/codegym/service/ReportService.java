package com.codegym.service;

import com.codegym.model.HireSession;
import com.codegym.model.Report;
import com.codegym.repository.HireSessionRepository;
import com.codegym.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final HireSessionRepository hireSessionRepository;

    @Transactional
    public Report saveReport(HireSession session, String content) {
        // Tạo báo cáo
        Report report = new Report();
        report.setHireSession(session);
        report.setCcdv(session.getCcdv());
        report.setUser(session.getUser());
        report.setReportContent(content);
        report.setCreatedAt(LocalDateTime.now());
        report.setStatus("PENDING"); // Trạng thái báo cáo: chờ duyệt

        // Lưu báo cáo
        Report savedReport = reportRepository.save(report);

        // Đổi trạng thái đơn thuê thành REVIEW_REPORT (chờ admin duyệt)
        session.setStatus("REVIEW_REPORT");
        hireSessionRepository.save(session);

        System.out.println(">>> Báo cáo được tạo. Đơn thuê #" + session.getId() + " chờ admin duyệt");

        return savedReport;
    }

    public java.util.List<Report> findAll() {
        return reportRepository.findAll();
    }
}