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

//  Lấy danh sách đơn thuê của user

    public Page<HireSession> getUserHireSessions(Long userId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());

        if (status != null && !status.trim().isEmpty()) {
            return userHireRepository.findByUserIdAndStatusOrderByStartTimeDesc(userId, status, pageable);
        }
        return userHireRepository.findByUserIdOrderByStartTimeDesc(userId, pageable);
    }

    //    Lấy chi tiết đơn thuê
    public HireSession getHireSessionById(Long sessionId) {
        return userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));
    }

    //   Hoàn thành đơn thuê
    @Transactional
    public HireSession completeHireSession(Long sessionId, Long userId) {
        HireSession session = userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));

        // Kiểm tra quyền
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        // Chỉ cho phép hoàn thành đơn ở trạng thái "Đã nhận"
        if (!"Đã nhận".equals(session.getStatus())) {
            throw new RuntimeException("Không thể hoàn thành đơn ở trạng thái: " + session.getStatus());
        }

        session.setStatus("Đã hoàn thành");
        session.setEndTime(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        return userHireRepository.save(session);
    }

    //    Cập nhật trạng thái đơn thuê
    @Transactional
    public HireSession updateHireSessionStatus(Long sessionId, Long userId, String newStatus) {
        HireSession session = userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));

        // Kiểm tra quyền
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        session.setStatus(newStatus);
        session.setUpdatedAt(LocalDateTime.now());

        if ("Đã hoàn thành".equals(newStatus) && session.getEndTime() == null) {
            session.setEndTime(LocalDateTime.now());
        }

        return userHireRepository.save(session);
    }

    //    Hủy đơn thuê
    @Transactional
    public void cancelHireSession(Long sessionId, Long userId) {
        HireSession session = userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));

        // Kiểm tra quyền
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        // Chỉ cho phép hủy đơn ở trạng thái "Chờ phản hồi"
        if (!"Chờ phản hồi".equals(session.getStatus())) {
            throw new RuntimeException("Chỉ có thể hủy đơn ở trạng thái 'Chờ phản hồi'");
        }

        userHireRepository.delete(session);
    }

    //    Thêm báo cáo/đánh giá cho đơn đã hoàn thành
    @Transactional
    public HireSession addUserReport(Long sessionId, Long userId, String report) {
        HireSession session = userHireRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê"));

        // Kiểm tra quyền
        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        // Chỉ cho phép báo cáo với đơn đã hoàn thành
        if (!"Đã hoàn thành".equals(session.getStatus())) {
            throw new RuntimeException("Chỉ có thể báo cáo với đơn đã hoàn thành");
        }

        session.setUserReport(report);
        session.setUpdatedAt(LocalDateTime.now());

        return userHireRepository.save(session);
    }

    //    Lấy thống kê đơn thuê của user
    public Map<String, Object> getUserStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // Đếm số đơn theo trạng thái
        Long waitingCount = userHireRepository.countByUserIdAndStatus(userId, "Chờ phản hồi");
        Long acceptedCount = userHireRepository.countByUserIdAndStatus(userId, "Đã nhận");
        Long completedCount = userHireRepository.countByUserIdAndStatus(userId, "Đã hoàn thành");

        // Tổng số đơn
        Long totalCount = (waitingCount != null ? waitingCount : 0L) +
                (acceptedCount != null ? acceptedCount : 0L) +
                (completedCount != null ? completedCount : 0L);

        // Tổng tiền đã chi
        Double totalAmount = userHireRepository.sumTotalPriceByUserId(userId);
        Double completedAmount = userHireRepository.sumTotalPriceByUserIdAndStatus(userId, "Đã hoàn thành");

        stats.put("total", totalCount);
        stats.put("waiting", waitingCount != null ? waitingCount : 0L);
        stats.put("accepted", acceptedCount != null ? acceptedCount : 0L);
        stats.put("completed", completedCount != null ? completedCount : 0L);
        stats.put("totalAmount", totalAmount != null ? totalAmount : 0.0);
        stats.put("completedAmount", completedAmount != null ? completedAmount : 0.0);

        return stats;
    }
}