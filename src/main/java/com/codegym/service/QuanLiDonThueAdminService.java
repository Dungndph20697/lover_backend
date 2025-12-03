package com.codegym.service;

import com.codegym.model.HireSession;
import com.codegym.model.Report;
import com.codegym.repository.QuanLiDonThueRepository;
import com.codegym.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuanLiDonThueAdminService {
    private final QuanLiDonThueRepository quanLiDonThueRepository;
    private final ReportRepository reportRepository;

    public Page<HireSession> getAllHireSessions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        return quanLiDonThueRepository.findAll(pageable);
    }

    public HireSession getHireSessionDetail(Long id) {
        return quanLiDonThueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));
    }

    // ✅ DUYỆT BÁO CÁO
    @Transactional
    public void approveReport(Long hireSessionId) {
        HireSession hireSession = getHireSessionDetail(hireSessionId);

        System.out.println(">>> Current status: " + hireSession.getStatus());

        // Kiểm tra trạng thái đơn
        String status = hireSession.getStatus();
        if (status == null || (!status.equals("REVIEW_REPORT") && !status.equals("review_report"))) {
            throw new RuntimeException("Đơn này không ở trạng thái duyệt báo cáo. Trạng thái hiện tại: " + status);
        }

        // ✅ FIX: Lấy danh sách báo cáo thay vì Optional
        List<Report> reports = reportRepository.findByHireSessionId(hireSessionId);

        if (reports.isEmpty()) {
            throw new RuntimeException("Không tìm thấy báo cáo");
        }

        // ✅ Duyệt tất cả các báo cáo (nếu có 2 cái thì cả 2 đều được duyệt)
        for (Report report : reports) {
            report.setStatus("APPROVED");
            reportRepository.save(report);
            System.out.println(">>> Approved report ID: " + report.getId());
        }

        // Cập nhật trạng thái đơn thuê sang REPORTED
        hireSession.setStatus("REPORTED");
        quanLiDonThueRepository.save(hireSession);
        System.out.println(">>> Hire session " + hireSessionId + " status changed to REPORTED");

        // Cập nhật số báo cáo của người CCDV
        Long ccdvId = hireSession.getCcdv().getId();
        Long reportCount = reportRepository.countByCcdvIdAndStatus(ccdvId, "APPROVED");
        System.out.println(">>> CCDV " + ccdvId + " now has " + reportCount + " approved reports");
    }

    // ✅ TỪ CHỐI BÁO CÁO
    @Transactional
    public void rejectReport(Long hireSessionId) {
        HireSession hireSession = getHireSessionDetail(hireSessionId);

        System.out.println(">>> Rejecting report for hire session: " + hireSessionId);

        // Kiểm tra trạng thái đơn
        String status = hireSession.getStatus();
        if (status == null || (!status.equals("REVIEW_REPORT") && !status.equals("review_report"))) {
            throw new RuntimeException("Đơn này không ở trạng thái duyệt báo cáo. Trạng thái hiện tại: " + status);
        }

        // ✅ FIX: Lấy danh sách rồi xóa
        List<Report> reports = reportRepository.findByHireSessionId(hireSessionId);

        if (!reports.isEmpty()) {
            reportRepository.deleteAll(reports);
            System.out.println(">>> Deleted " + reports.size() + " report(s) for hire session: " + hireSessionId);
        }

        // Chuyển trạng thái đơn về COMPLETED
        hireSession.setStatus("COMPLETED");
        quanLiDonThueRepository.save(hireSession);
        System.out.println(">>> Hire session " + hireSessionId + " status changed to COMPLETED");
    }
}