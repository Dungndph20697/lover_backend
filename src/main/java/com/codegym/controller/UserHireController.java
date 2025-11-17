package com.codegym.controller;

import com.codegym.model.HireSession;
import com.codegym.service.UserHireService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/hire-sessions")
@RequiredArgsConstructor
public class UserHireController {

    private final UserHireService userHireService;

    //    Lấy danh sách đơn đã thuê
    @GetMapping
    public ResponseEntity<Page<HireSession>> getUserHireSessions(
            @RequestParam Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<HireSession> sessions = userHireService.getUserHireSessions(userId, status, page, size);
        return ResponseEntity.ok(sessions);
    }

    //    Lấy chi tiết đơn thuê
    @GetMapping("/{sessionId}")
    public ResponseEntity<HireSession> getHireSessionById(@PathVariable Long sessionId) {
        HireSession session = userHireService.getHireSessionById(sessionId);
        return ResponseEntity.ok(session);
    }

    //    Hoàn thành đơn
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<HireSession> completeHireSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {

        HireSession session = userHireService.completeHireSession(sessionId, userId);
        return ResponseEntity.ok(session);
    }

    //    Cập nhật trạng thái
    @PatchMapping("/{sessionId}/status")
    public ResponseEntity<HireSession> updateStatus(
            @PathVariable Long sessionId,
            @RequestParam Long userId,
            @RequestParam String status) {

        HireSession session = userHireService.updateHireSessionStatus(sessionId, userId, status);
        return ResponseEntity.ok(session);
    }

    //    Hủy đơn thuê
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> cancelHireSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {

        userHireService.cancelHireSession(sessionId, userId);
        return ResponseEntity.noContent().build();
    }

    //   Thêm báo cáo
    @PostMapping("/{sessionId}/report")
    public ResponseEntity<HireSession> addUserReport(
            @PathVariable Long sessionId,
            @RequestParam Long userId,
            @RequestBody Map<String, String> request) {

        String report = request.get("report");
        if (report == null || report.trim().isEmpty()) {
            throw new RuntimeException("Nội dung báo cáo không được để trống");
        }

        HireSession session = userHireService.addUserReport(sessionId, userId, report);
        return ResponseEntity.ok(session);
    }

    //    Lấy thống kê
    @GetMapping("/statistics/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        Map<String, Object> stats = userHireService.getUserStatistics(userId);
        return ResponseEntity.ok(stats);
    }

    //    Exception Handler
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
