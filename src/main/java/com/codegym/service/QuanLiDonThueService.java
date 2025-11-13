package com.codegym.service;

import com.codegym.model.CcdvProfile;
import com.codegym.model.HireSession;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.QuanLiDonThueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QuanLiDonThueService {

    private final QuanLiDonThueRepository quanLiDonThueRepository;
    private final CcdvProfileRepository ccdvProfileRepository;

    // LẤY DANH SÁCH ĐƠN THUÊ
    public Map<String, Object> getCcdvSessions(Long ccdvId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<HireSession> sessions = quanLiDonThueRepository.findByCcdvIdOrderByStartTimeDesc(ccdvId);
            response.put("success", true);
            response.put("data", sessions);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lấy danh sách: " + e.getMessage());
            return response;
        }
    }

    // XÁC NHẬN NHẬN ĐƠN (PENDING -> ACCEPTED) + gửi tin nhắn
    @Transactional
    public Map<String, Object> acceptSession(Long sessionId, Long ccdvId) {
        Map<String, Object> response = new HashMap<>();

        Optional<HireSession> sessionOpt = quanLiDonThueRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Đơn thuê không tồn tại");
            return response;
        }

        HireSession session = sessionOpt.get();

        // kiểm tra quyền
        if (!Objects.equals(session.getCcdv().getId(), ccdvId)) {
            response.put("success", false);
            response.put("message", "Bạn không có quyền xử lý đơn này");
            return response;
        }

        // Chỉ được xác nhận khi đang ở trạng thái PENDING
        if (!"PENDING".equals(session.getStatus())) {
            response.put("success", false);
            response.put("message", "Chỉ có thể xác nhận đơn ở trạng thái 'Chờ phản hồi'");
            return response;
        }

        // Cập nhật trạng thái sang “ĐÃ NHẬN”
        session.setStatus("ACCEPTED");
        session.setUpdatedAt(LocalDateTime.now());
        quanLiDonThueRepository.save(session);

        // --- Gửi tin nhắn cho người thuê ---
        System.out.println("Tin nhắn gửi cho người thuê: Người yêu mà bạn thuê đã xác nhận đơn rồi");

        response.put("success", true);
        response.put("message", "Đã xác nhận nhận đơn");
        response.put("data", session);
        return response;
    }

    // HOÀN THÀNH ĐƠN VÀ NHẬN TIỀN
    @Transactional
    public Map<String, Object> completeSession(Long sessionId, Long ccdvId) {
        Map<String, Object> response = new HashMap<>();

        Optional<HireSession> sessionOpt = quanLiDonThueRepository.findById(sessionId);

        if (sessionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Đơn thuê không tồn tại");
            return response;
        }

        HireSession session = sessionOpt.get();

        if (!session.getCcdv().getId().equals(ccdvId)) {
            response.put("success", false);
            response.put("message", "Bạn không có quyền xử lý đơn này");
            return response;
        }

        if (!"ACCEPTED".equals(session.getStatus())) {
            response.put("success", false);
            response.put("message", "Chỉ có thể hoàn thành đơn ở trạng thái 'Đã nhận'");
            return response;
        }

        session.setStatus("COMPLETED");
        session.setUpdatedAt(LocalDateTime.now());
        quanLiDonThueRepository.save(session);

        // Tăng số lần được thuê trong profile
//        Optional<CcdvProfile> profileOpt = ccdvProfileRepository.findByUserId(ccdvId);
//        profileOpt.ifPresent(profile -> {
//            profile.setHireCount(profile.getHireCount() + 1);
//            ccdvProfileRepository.save(profile);
//        });

        // --- Gửi tin nhắn cho người thuê ---
        System.out.println("Tin nhắn gửi cho người thuê: Người yêu của bạn đã hoàn thành đơn thuê");

        response.put("success", true);
        response.put("message", "Đã hoàn thành đơn và nhận tiền");
        response.put("data", session);
        return response;
    }

    // BÁO CÁO VỀ KHÁCH HÀNG (COMPLETED -> REPORTED) + gửi tin nhắn
    @Transactional
    public Map<String, Object> reportClient(Long sessionId, Long ccdvId, String report) {
        Map<String, Object> response = new HashMap<>();

        if (report == null || report.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Nội dung báo cáo không được để trống");
            return response;
        }

        Optional<HireSession> sessionOpt = quanLiDonThueRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Đơn thuê không tồn tại");
            return response;
        }

        HireSession session = sessionOpt.get();

        if (!session.getCcdv().getId().equals(ccdvId)) {
            response.put("success", false);
            response.put("message", "Bạn không có quyền xử lý đơn này");
            return response;
        }

        if (!"COMPLETED".equals(session.getStatus())) {
            response.put("success", false);
            response.put("message", "Chỉ có thể báo cáo đơn ở trạng thái 'Đã hoàn thành'");
            return response;
        }

        session.setUserReport(report);
        session.setStatus("REPORTED");
        session.setUpdatedAt(LocalDateTime.now());
        quanLiDonThueRepository.save(session);

        // --- Gửi tin nhắn cho người thuê ---
        // Lấy email người thuê
        String userEmail = session.getUser().getEmail();
        response.put("userEmail", userEmail);
        response.put("success", true);
        response.put("message", "Đơn thuê đã được xác nhận thành công");

        return response;
//        System.out.println("Tin nhắn gửi cho người thuê: CCDV đã gửi báo cáo về bạn");
//
//        response.put("success", true);
//        response.put("message", "Đã gửi báo cáo thành công");
//        response.put("data", session);
//        return response;
    }

    // LẤY CHI TIẾT ĐƠN THUÊ
    public Map<String, Object> getSessionDetail(Long sessionId) {
        Map<String, Object> response = new HashMap<>();

        Optional<HireSession> sessionOpt = quanLiDonThueRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Đơn thuê không tồn tại");
            return response;
        }

        response.put("success", true);
        response.put("data", sessionOpt.get());
        return response;
    }

    // THỐNG KÊ
    public Map<String, Object> getCcdvStatistics(Long ccdvId) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> stats = new HashMap<>();

        // Lấy tất cả phiên thuê của CCDV
        List<HireSession> allSessions = quanLiDonThueRepository.findByCcdvIdOrderByStartTimeDesc(ccdvId);

        // Thống kê số lượng theo trạng thái
        long pending = allSessions.stream().filter(s -> "PENDING".equals(s.getStatus())).count();
        long accepted = allSessions.stream().filter(s -> "ACCEPTED".equals(s.getStatus())).count();
        long completed = allSessions.stream().filter(s -> "COMPLETED".equals(s.getStatus())).count();
        long reported = allSessions.stream().filter(s -> "REPORTED".equals(s.getStatus())).count();

        // Tổng số đơn
        long tongDon = allSessions.size();

        // Tổng thu nhập (chỉ tính các đơn COMPLETED hoặc REPORTED)
        double tongThu = allSessions.stream()
                .filter(s -> "COMPLETED".equals(s.getStatus()) || "REPORTED".equals(s.getStatus()))
                .mapToDouble(HireSession::getTotalPrice)
                .sum();

        // Put dữ liệu vào stats
        stats.put("tongDon", tongDon);
        stats.put("tongThu", tongThu);
        stats.put("pending", pending);
        stats.put("accepted", accepted);
        stats.put("completed", completed);
        stats.put("reported", reported);

        response.put("success", true);
        response.put("data", stats);

        return response;
    }

    // CHỈNH SỬA PHẢN HỒI VỀ NGƯỜI THUÊ (chỉ khi đơn ở trạng thái COMPLETED)
    @Transactional
    public Map<String, Object> updateUserFeedback(Long sessionId, Long ccdvId, String feedback) {
        Map<String, Object> response = new HashMap<>();

        if (feedback == null || feedback.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Nội dung phản hồi không được để trống");
            return response;
        }

        Optional<HireSession> sessionOpt = quanLiDonThueRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Đơn thuê không tồn tại");
            return response;
        }

        HireSession session = sessionOpt.get();

        // Kiểm tra quyền
        if (!session.getCcdv().getId().equals(ccdvId)) {
            response.put("success", false);
            response.put("message", "Bạn không có quyền chỉnh sửa phản hồi của đơn này");
            return response;
        }

        // Chỉ cho phép chỉnh sửa khi đơn ở trạng thái COMPLETED
        if (!"COMPLETED".equals(session.getStatus())) {
            response.put("success", false);
            response.put("message", "Chỉ có thể chỉnh sửa phản hồi khi đơn ở trạng thái 'Đã hoàn thành'");
            return response;
        }

        // Cập nhật phản hồi
        session.setUserReport(feedback);
        session.setUpdatedAt(LocalDateTime.now());
        quanLiDonThueRepository.save(session);

        response.put("success", true);
        response.put("message", "Đã cập nhật phản hồi thành công");
        response.put("data", session);
        return response;
    }

}
