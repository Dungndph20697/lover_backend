package com.codegym.controller.admin;

import com.codegym.dto.CcdvDetailAdminDTO;
import com.codegym.dto.UserListDTO;
import com.codegym.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminQuanLyUserController {
    @Autowired
    private IAdminService adminService;

    // Danh sách người dùng (trừ ADMIN)
    @GetMapping("/users")
    public ResponseEntity<List<UserListDTO>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsersExceptAdmin());
    }

    // Lọc theo role ⇒ USER hoặc SERVICE_PROVIDER
    @GetMapping("/users/filter")
    public ResponseEntity<List<UserListDTO>> filterByRole(@RequestParam String role) {
        return ResponseEntity.ok(adminService.filterUsersByRole(role));
    }

    // Chi tiết CCDV
    @GetMapping("/ccdv/{id}")
    public ResponseEntity<CcdvDetailAdminDTO> getCcdvDetail(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCcdvDetail(id));
    }

    // Khóa user
    @PutMapping("/user/lock/{id}")
    public ResponseEntity<?> lockUser(@PathVariable Long id) {
        adminService.lockUser(id);
        return ResponseEntity.ok("User locked successfully");
    }

    // Mở khóa user
    @PutMapping("/user/unlock/{id}")
    public ResponseEntity<?> unlockUser(@PathVariable Long id) {
        adminService.unlockUser(id);
        return ResponseEntity.ok("User unlocked successfully");
    }
}
