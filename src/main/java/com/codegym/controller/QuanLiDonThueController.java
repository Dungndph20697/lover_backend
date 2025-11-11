package com.codegym.controller;

import com.codegym.service.EmailService;
import com.codegym.service.QuanLiDonThueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ccdv/hire-sessions")
@RequiredArgsConstructor
public class QuanLiDonThueController {

    private final QuanLiDonThueService quanLiDonThueService;
    private final EmailService emailService;

    // ✅ Endpoint test gửi email (chỉ để test trên Postman)
    @PostMapping("/test-email")
    public ResponseEntity<Map<String, Object>> testSendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text) {
        try {
            emailService.sendEmail(to, subject, text);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Email đã được gửi thành công đến " + to
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Gửi email thất bại: " + e.getMessage()
            ));
        }
    }

    // Lấy danh sách đơn thuê của CCDV
    @GetMapping("/{ccdvId}")
    public ResponseEntity<Map<String, Object>> getSessions(@PathVariable Long ccdvId) {
        return ResponseEntity.ok(quanLiDonThueService.getCcdvSessions(ccdvId));
    }

    // Xác nhận nhận đơn và gửi tin nhắn thông báo
    @PutMapping("/{sessionId}/accept")
    public ResponseEntity<Map<String, Object>> acceptSession(
            @PathVariable Long sessionId,
            @RequestParam Long ccdvId) {

        Map<String, Object> result = quanLiDonThueService.acceptSession(sessionId, ccdvId);

        if (Boolean.TRUE.equals(result.get("success"))) {
            String userEmail = (String) result.get("userEmail");

            if (userEmail != null && !userEmail.isEmpty()) {
                emailService.sendEmail(
                        userEmail,
                        "Xác nhận đơn thuê thành công",
                        "Người yêu mà bạn thuê đã xác nhận đơn của bạn rồi 💌"
                );
            }
        }

        return ResponseEntity.ok(result);
    }

    // Hoàn thành và nhận tiền
    @PutMapping("/{sessionId}/complete")
    public ResponseEntity<Map<String, Object>> completeSession(
            @PathVariable Long sessionId,
            @RequestParam Long ccdvId) {
        return ResponseEntity.ok(quanLiDonThueService.completeSession(sessionId, ccdvId));
    }

    // Báo cáo về khách hàng
    @PutMapping("/{sessionId}/report")
    public ResponseEntity<Map<String, Object>> reportClient(
            @PathVariable Long sessionId,
            @RequestParam Long ccdvId,
            @RequestBody Map<String, String> payload) {
        String report = payload.get("report");
        return ResponseEntity.ok(quanLiDonThueService.reportClient(sessionId, ccdvId, report));
    }

    // Lấy chi tiết đơn
    @GetMapping("/detail/{sessionId}")
    public ResponseEntity<Map<String, Object>> getDetail(@PathVariable Long sessionId) {
        return ResponseEntity.ok(quanLiDonThueService.getSessionDetail(sessionId));
    }

    // Thống kê
    @GetMapping("/statistics/{ccdvId}")
    public ResponseEntity<Map<String, Object>> getStatistics(@PathVariable Long ccdvId) {
        return ResponseEntity.ok(quanLiDonThueService.getCcdvStatistics(ccdvId));
    }
}
