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
@CrossOrigin(origins = "*")
public class UserHireController {
    
    private final UserHireService userHireService;
    
    /**
     * GET /api/user/hire-sessions - Lấy danh sách đơn đã thuê
     * Params: userId, status (optional), page (default 0), size (default 10)
     */
    @GetMapping
    public ResponseEntity<Page<HireSession>> getUserHireSessions(
            @RequestParam Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<HireSession> sessions = userHireService.getUserHireSessions(userId, status, page, size);
        return ResponseEntity.ok(sessions);
    }
    
    /**
     * GET /api/user/hire-sessions/{sessionId} - Lấy chi tiết đơn thuê
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<HireSession> getHireSessionById(@PathVariable Long sessionId) {
        HireSession session = userHireService.getHireSessionById(sessionId);
        return ResponseEntity.ok(session);
    }
    
    /**
     * PATCH /api/user/hire-sessions/{sessionId}/complete - Hoàn thành đơn
     * Params: userId
     */
    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<HireSession> completeHireSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        
        HireSession session = userHireService.completeHireSession(sessionId, userId);
        return ResponseEntity.ok(session);
    }
    
    /**
     * PATCH /api/user/hire-sessions/{sessionId}/status - Cập nhật trạng thái
     * Params: userId, status
     */
    @PatchMapping("/{sessionId}/status")
    public ResponseEntity<HireSession> updateStatus(
            @PathVariable Long sessionId,
            @RequestParam Long userId,
            @RequestParam String status) {
        
        HireSession session = userHireService.updateHireSessionStatus(sessionId, userId, status);
        return ResponseEntity.ok(session);
    }
    
    /**
     * DELETE /api/user/hire-sessions/{sessionId} - Hủy đơn thuê
     * Params: userId
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> cancelHireSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        
        userHireService.cancelHireSession(sessionId, userId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * POST /api/user/hire-sessions/{sessionId}/report - Thêm báo cáo
     * Params: userId
     * Body: { "report": "..." }
     */
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
    
    /**
     * GET /api/user/hire-sessions/statistics/{userId} - Lấy thống kê
     */
    @GetMapping("/statistics/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        Map<String, Object> stats = userHireService.getUserStatistics(userId);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Exception Handler
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
