package com.codegym.controller;

import com.codegym.service.EmailNotificationService;
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
    private final EmailNotificationService emailNotificationService;

    @PostMapping("/test-email")
    public ResponseEntity<Map<String, Object>> testSendSimpleEmail(
            @RequestBody Map<String, String> emailData) {
        try {
            String to = emailData.get("to");
            emailNotificationService.sendSimpleEmail(to, "Test User", "Test CCDV", 999L);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Email text đã được gửi thành công đến " + to
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Gửi email thất bại: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/test-email-html")
    public ResponseEntity<Map<String, Object>> testSendHtmlEmail(
            @RequestBody Map<String, Object> emailData) {
        try {
            String to = (String) emailData.get("to");
            String customerName = (String) emailData.getOrDefault("customerName", "Khách hàng");
            String ccdvName = (String) emailData.getOrDefault("ccdvName", "CCDV");
            Long sessionId = emailData.containsKey("sessionId")
                    ? Long.valueOf(emailData.get("sessionId").toString())
                    : 999L;

            emailNotificationService.sendOrderConfirmationEmail(
                    to,
                    customerName,
                    ccdvName,
                    sessionId
            );

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Email HTML đã được gửi thành công đến " + to,
                    "preview", "Kiểm tra hộp thư đến của " + to
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Gửi email thất bại: " + e.getMessage(),
                    "error", e.toString()
            ));
        }
    }

    @GetMapping("/{ccdvId}")
    public ResponseEntity<Map<String, Object>> getSessions(@PathVariable("ccdvId") Long ccdvId) {
        return ResponseEntity.ok(quanLiDonThueService.getCcdvSessions(ccdvId));
    }

    @GetMapping("/statistics/{ccdvId}")
    public ResponseEntity<Map<String, Object>> getStatistics(@PathVariable Long ccdvId) {
        return ResponseEntity.ok(quanLiDonThueService.getCcdvStatistics(ccdvId));
    }

    @GetMapping("/detail/{sessionId}")
    public ResponseEntity<Map<String, Object>> getDetail(@PathVariable Long sessionId) {
        return ResponseEntity.ok(quanLiDonThueService.getSessionDetail(sessionId));
    }

    @PutMapping("/{sessionId}/accept")
    public ResponseEntity<Map<String, Object>> acceptSession(
            @PathVariable Long sessionId,
            @RequestParam Long ccdvId) {

        Map<String, Object> result = quanLiDonThueService.acceptSession(sessionId, ccdvId);

        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/{sessionId}/complete")
    public ResponseEntity<Map<String, Object>> completeSession(
            @PathVariable Long sessionId,
            @RequestParam Long ccdvId) {

        Map<String, Object> result = quanLiDonThueService.completeSession(sessionId, ccdvId);

        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/{sessionId}/report")
    public ResponseEntity<Map<String, Object>> reportClient(
            @PathVariable Long sessionId,
            @RequestBody Map<String, Object> payload) {

        Long ccdvId = Long.valueOf(payload.get("ccdvId").toString());
        String report = (String) payload.get("report");

        Map<String, Object> result = quanLiDonThueService.reportClient(sessionId, ccdvId, report);

        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/{sessionId}/feedback")
    public ResponseEntity<Map<String, Object>> updateFeedback(
            @PathVariable Long sessionId,
            @RequestBody Map<String, Object> payload) {

        Long ccdvId = Long.valueOf(payload.get("ccdvId").toString());
        String feedback = (String) payload.get("feedback");

        Map<String, Object> result = quanLiDonThueService.updateUserFeedback(sessionId, ccdvId, feedback);

        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}