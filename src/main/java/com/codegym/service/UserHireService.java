package com.codegym.service;

import com.codegym.model.HireSession;
import com.codegym.repository.UserHireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserHireService {

    private final UserHireRepository userHireRepository;

    // ✅ Lấy danh sách đơn thuê của user
    public Page<HireSession> getUserHireSessions(Long userId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());

        if (status != null && !status.trim().isEmpty()) {
            return userHireRepository.findByUserIdAndStatusOrderByStartTimeDesc(userId, status, pageable);
        }
        return userHireRepository.findByUserIdOrderByStartTimeDesc(userId, pageable);
    }

    // ✅ Lấy chi tiết đơn thuê
    public HireSession getHireSessionById(Long sessionId) {
        return userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));
    }

    // ✅ Hoàn thành đơn thuê (ACCEPTED -> COMPLETED)
    @Transactional
    public HireSession completeHireSession(Long sessionId, Long userId) {
        HireSession session = userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));

        // Kiểm tra quyền
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        // ✅ FIX: Dùng ENUM thay vì tiếng Việt
        if (!"ACCEPTED".equals(session.getStatus())) {
            throw new RuntimeException("Chỉ có thể hoàn thành đơn ở trạng thái 'Đã nhận'");
        }

        session.setStatus("COMPLETED");
        session.setUpdatedAt(LocalDateTime.now());

        return userHireRepository.save(session);
    }

    // ✅ Cập nhật trạng thái đơn thuê
    @Transactional
    public HireSession updateHireSessionStatus(Long sessionId, Long userId, String newStatus) {
        HireSession session = userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));

        // Kiểm tra quyền
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        // ✅ Validate status
        validateStatus(newStatus);

        session.setStatus(newStatus);
        session.setUpdatedAt(LocalDateTime.now());

        return userHireRepository.save(session);
    }

    // ✅ Hủy đơn thuê
    @Transactional
    public void cancelHireSession(Long sessionId, Long userId) {
        HireSession session = userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));

        // Kiểm tra quyền
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        // ✅ FIX: Dùng ENUM
        if (!"PENDING".equals(session.getStatus())) {
            throw new RuntimeException("Chỉ có thể hủy đơn ở trạng thái 'Chờ phản hồi'");
        }

        userHireRepository.delete(session);
    }

    // ✅ Thêm báo cáo - FIX logic
//    @Transactional
//    public HireSession addUserReport(Long sessionId, Long userId, String report) {
//        HireSession session = userHireRepository.findById(sessionId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));
//
//        // Kiểm tra quyền
//        if (!session.getUser().getId().equals(userId)) {
//            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
//        }
//
//        // ✅ FIX: Chỉ cho phép báo cáo khi COMPLETED (chưa gửi báo cáo)
//        // Không được phép sửa báo cáo khi đã gửi (REVIEW_REPORT hoặc REPORTED)
//        if (!"COMPLETED".equals(session.getStatus())) {
//            throw new RuntimeException("Chỉ có thể báo cáo với đơn ở trạng thái 'Đã hoàn thành'");
//        }
//
//        session.setUserReport(report);
//        session.setStatus("REVIEW_REPORT");  // ✅ Chuyển sang chờ duyệt
//        session.setUpdatedAt(LocalDateTime.now());
//
//        return userHireRepository.save(session);
//    }

    // ✅ Lấy thống kê đơn thuê của user - FIX
    public Map<String, Object> getUserStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // ✅ FIX: Dùng ENUM chính xác
        Long pendingCount = userHireRepository.countByUserIdAndStatus(userId, "PENDING");
        Long acceptedCount = userHireRepository.countByUserIdAndStatus(userId, "ACCEPTED");
        Long completedCount = userHireRepository.countByUserIdAndStatus(userId, "COMPLETED");
        Long reviewReportCount = userHireRepository.countByUserIdAndStatus(userId, "REVIEW_REPORT");
        Long reportedCount = userHireRepository.countByUserIdAndStatus(userId, "REPORTED");

        // Tổng số đơn
        Long totalCount = (pendingCount != null ? pendingCount : 0L) +
                (acceptedCount != null ? acceptedCount : 0L) +
                (completedCount != null ? completedCount : 0L) +
                (reviewReportCount != null ? reviewReportCount : 0L) +
                (reportedCount != null ? reportedCount : 0L);

        // ✅ Tổng tiền - chỉ tính COMPLETED, REPORTED (không tính REVIEW_REPORT vì chưa qua duyệt)
        Double totalAmount = userHireRepository.sumTotalPriceByUserId(userId);
        Double completedAmount = userHireRepository.sumTotalPriceByUserIdAndStatus(userId, "COMPLETED");

        stats.put("total", totalCount);
        stats.put("pending", pendingCount != null ? pendingCount : 0L);
        stats.put("accepted", acceptedCount != null ? acceptedCount : 0L);
        stats.put("completed", completedCount != null ? completedCount : 0L);
        stats.put("reviewReport", reviewReportCount != null ? reviewReportCount : 0L);
        stats.put("reported", reportedCount != null ? reportedCount : 0L);
        stats.put("totalAmount", totalAmount != null ? totalAmount : 0.0);
        stats.put("completedAmount", completedAmount != null ? completedAmount : 0.0);

        return stats;
    }

    // ✅ Helper: Validate status
    private void validateStatus(String status) {
        if (!status.matches("PENDING|ACCEPTED|COMPLETED|REVIEW_REPORT|REPORTED")) {
            throw new RuntimeException("Trạng thái không hợp lệ: " + status);
        }
    }
}