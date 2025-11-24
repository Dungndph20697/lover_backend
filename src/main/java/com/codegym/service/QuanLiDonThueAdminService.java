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

    // Duyệt báo cáo
    @Transactional
    public void approveReport(Long hireSessionId) {
        HireSession hireSession = getHireSessionDetail(hireSessionId);

        // Debug: in ra status hiện tại
        System.out.println(">>> Current status: " + hireSession.getStatus());

        // Kiểm tra trạng thái đơn - chấp nhận cả uppercase và lowercase
        String status = hireSession.getStatus();
        if (status == null || (!status.equals("REVIEW_REPORT") && !status.equals("review_report"))) {
            throw new RuntimeException("Đơn này không ở trạng thái duyệt báo cáo. Trạng thái hiện tại: " + status);
        }

        // Tìm báo cáo liên quan
        Report report = reportRepository.findByHireSessionId(hireSessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo cáo"));

        // Cập nhật thông tin báo cáo
        report.setStatus("APPROVED");
        reportRepository.save(report);

        // Cập nhật trạng thái đơn thuê
        hireSession.setStatus("COMPLETED");
        quanLiDonThueRepository.save(hireSession);

        // Cập nhật số báo cáo của người CCDV
        Long ccdvId = hireSession.getCcdv().getId();
        Long reportCount = reportRepository.countByCcdvIdAndStatus(ccdvId, "APPROVED");
        // Cập nhật trong User entity nếu có field reportCount
        // user.setReportCount(reportCount);
    }

    // Từ chối báo cáo
    @Transactional
    public void rejectReport(Long hireSessionId) {
        HireSession hireSession = getHireSessionDetail(hireSessionId);

        // Kiểm tra trạng thái đơn
        String status = hireSession.getStatus();
        if (status == null || (!status.equals("REVIEW_REPORT") && !status.equals("review_report"))) {
            throw new RuntimeException("Đơn này không ở trạng thái duyệt báo cáo. Trạng thái hiện tại: " + status);
        }

        // Xóa tất cả báo cáo liên quan đến đơn này
        reportRepository.deleteByHireSessionId(hireSessionId);
        System.out.println(">>> Deleted reports for hire session: " + hireSessionId);

        // Chuyển trạng thái đơn về COMPLETED
        hireSession.setStatus("COMPLETED");
        quanLiDonThueRepository.save(hireSession);
        System.out.println(">>> Hire session " + hireSessionId + " status changed to COMPLETED");
    }
}
