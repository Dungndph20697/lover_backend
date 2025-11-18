package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.model.WithdrawRequest;
import com.codegym.repository.UserRepository;
import com.codegym.repository.WithdrawRequestRepository;
import com.codegym.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/withdraw")
@RequiredArgsConstructor
public class WithdrawController {

    private final UserRepository userRepo;
    private final WithdrawService withdrawService;
    private final WithdrawRequestRepository withdrawRepo;

    // 1) CCDV GỬI YÊU CẦU RÚT TIỀN
    @PostMapping("/request")
    public ResponseEntity<?> requestWithdraw(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> body) {

        Double amount = Double.valueOf(body.get("amount").toString());
        String bankName = body.get("bankName").toString();
        String bankAccountNumber = body.get("bankAccountNumber").toString();
        String bankAccountName = body.get("bankAccountName").toString();

        User user = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                withdrawService.createWithdrawRequest(
                        user, amount,
                        bankName,
                        bankAccountNumber,
                        bankAccountName
                )
        );
    }

    // 2) CCDV XÁC MINH OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, Object> body) {

        Long requestId = Long.valueOf(body.get("requestId").toString());
        String otp = body.get("otp").toString();

        boolean ok = withdrawService.verifyOtp(requestId, otp);

        if (!ok)
            return ResponseEntity.badRequest().body(Map.of("message", "OTP sai"));

        return ResponseEntity.ok(Map.of("success", true));
    }

    // 3) CCDV XEM LỊCH SỬ RÚT TIỀN
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<WithdrawRequest> history =
                withdrawRepo.findByUserAndStatusNotOrderByCreatedAtDesc(user, "PENDING");

        return ResponseEntity.ok(history);
    }

    // 4 gửi lại OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, Object> body) {
        Long requestId = Long.valueOf(body.get("requestId").toString());

        boolean ok = withdrawService.resendOtp(requestId);

        if (!ok)
            return ResponseEntity.badRequest().body(Map.of("message", "Yêu cầu không hợp lệ"));

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "OTP đã được gửi lại"
        ));
    }
}
