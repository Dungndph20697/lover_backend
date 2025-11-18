package com.codegym.controller.admin;

import com.codegym.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// lớp admin duyệt yêu cầu rút tiền
@RestController
@RequestMapping("/api/admin/withdraw")
@RequiredArgsConstructor
public class AdminWithdrawController {

    private final WithdrawService withdrawService;

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        return ResponseEntity.ok(withdrawService.approveWithdraw(id));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        return ResponseEntity.ok(withdrawService.rejectWithdraw(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(withdrawService.getAdminList());
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(withdrawService.searchAdmin(keyword));
    }
}

