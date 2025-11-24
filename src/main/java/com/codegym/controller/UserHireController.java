package com.codegym.controller;

import com.codegym.model.HireSession;
import com.codegym.service.UserHireService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/hire-sessions")
@RequiredArgsConstructor
public class UserHireController {

    private final UserHireService userHireService;

    // ✅ Lấy thống kê
    @GetMapping("/statistics/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = userHireService.getUserStatistics(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ✅ Lấy danh sách đơn đã thuê
    @GetMapping
    public ResponseEntity<Page<HireSession>> getUserHireSessions(
            @RequestParam Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<HireSession> sessions = userHireService.getUserHireSessions(userId, status, page, size);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ✅ Lấy chi tiết đơn thuê
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getHireSessionById(@PathVariable Long sessionId) {
        try {
            HireSession session = userHireService.getHireSessionById(sessionId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ✅ Hoàn thành đơn (ACCEPTED -> COMPLETED)
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<?> completeHireSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {

        try {
            HireSession session = userHireService.completeHireSession(sessionId, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã hoàn thành đơn thuê");
            response.put("data", session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ✅ Cập nhật trạng thái (không cần dùng, sử dụng các endpoint cụ thể thay)
    @PatchMapping("/{sessionId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long sessionId,
            @RequestParam Long userId,
            @RequestParam String status) {

        try {
            HireSession session = userHireService.updateHireSessionStatus(sessionId, userId, status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã cập nhật trạng thái");
            response.put("data", session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ✅ Hủy đơn thuê (PENDING -> xóa)
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> cancelHireSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {

        try {
            userHireService.cancelHireSession(sessionId, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã hủy đơn thuê");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ✅ Thêm báo cáo (COMPLETED -> REVIEW_REPORT)
    @PostMapping("/{sessionId}/report")
    public ResponseEntity<?> addUserReport(
            @PathVariable Long sessionId,
            @RequestParam Long userId,
            @RequestBody Map<String, String> request) {

        try {
            String report = request.get("report");

            if (report == null || report.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Nội dung báo cáo không được để trống");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            HireSession session = userHireService.addUserReport(sessionId, userId, report);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Báo cáo đã được gửi, chờ admin xem xét");
            response.put("data", session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ✅ Global Exception Handler
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "Có lỗi xảy ra: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}