package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.model.Wallet;
import com.codegym.repository.UserRepository;
import com.codegym.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final UserRepository userRepository;
    private final WalletService walletService;

    // 1. API LẤY SỐ DƯ
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        Wallet wallet = walletService.getWalletByUser(user);

        return ResponseEntity.ok(Map.of(
                "balance", wallet.getBalance()
        ));
    }

    // 2. API NẠP TIỀN THỦ CÔNG (TEST)
    @PostMapping("/topup")
    public ResponseEntity<?> topUp(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> request
    ) {
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        Double amount = Double.valueOf(request.get("amount").toString());

        Wallet wallet = walletService.topUp(user, amount);

        return ResponseEntity.ok(Map.of(
                "message", "Nạp tiền thành công!",
                "newBalance", wallet.getBalance()
        ));
    }
}
