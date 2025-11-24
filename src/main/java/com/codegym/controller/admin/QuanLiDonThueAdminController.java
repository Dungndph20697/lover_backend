package com.codegym.controller.admin;

import com.codegym.model.HireSession;
import com.codegym.service.QuanLiDonThueAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/hire-sessions")
@RequiredArgsConstructor
public class QuanLiDonThueAdminController {

    private final QuanLiDonThueAdminService adminService;

    @GetMapping
    public Page<HireSession> getAllHireSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminService.getAllHireSessions(page, size);
    }

    // Lấy chi tiết đơn thuê
    @GetMapping("/{id}")
    public ResponseEntity<HireSession> getHireSessionDetail(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getHireSessionDetail(id));
    }

    // Duyệt báo cáo
    @PostMapping("/{id}/approve-report")
    public ResponseEntity<?> approveReport(@PathVariable Long id) {
        adminService.approveReport(id);
        return ResponseEntity.ok(java.util.Map.of("message", "Duyệt báo cáo thành công"));
    }

    // Từ chối báo cáo
    @PostMapping("/{id}/reject-report")
    public ResponseEntity<?> rejectReport(@PathVariable Long id) {
        adminService.rejectReport(id);
        return ResponseEntity.ok(java.util.Map.of("message", "Từ chối báo cáo thành công"));
    }
}
