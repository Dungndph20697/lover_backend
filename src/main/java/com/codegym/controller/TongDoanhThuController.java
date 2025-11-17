package com.codegym.controller;

import com.codegym.dto.RevenueRangeRequestDTO;
import com.codegym.dto.RevenueResponseDTO;
import com.codegym.service.tongdoanhthu.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;

@RestController
@RequestMapping("/api/revenue")
public class TongDoanhThuController {
    @Autowired
    private RevenueService revenueService;

    @GetMapping("/today")
    public RevenueResponseDTO revenueToday(Principal principal) {
        Double revenue = revenueService.revenueToday(principal.getName());
        LocalDate today = LocalDate.now();
        return new RevenueResponseDTO(revenue, "VND",
                today.toString(), today.toString());
    }

    @GetMapping("/month")
    public RevenueResponseDTO revenueMonth(Principal principal) {
        YearMonth ym = YearMonth.now();
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);
        Double revenue = revenueService.revenueThisMonth(principal.getName());
        return new RevenueResponseDTO(revenue, "VND",
                start.toLocalDate().toString(), end.toLocalDate().toString());
    }

    @PostMapping("/range")
    public ResponseEntity<RevenueResponseDTO> revenueRangeTotal(
            @RequestBody RevenueRangeRequestDTO dto,
            Principal principal) {

        LocalDateTime start = LocalDate.parse(dto.getStart()).atStartOfDay();
        LocalDateTime end = LocalDate.parse(dto.getEnd()).atTime(23, 59, 59);

        Double revenue = revenueService.revenueInRange(principal.getName(), start, end);

        return ResponseEntity.ok(new RevenueResponseDTO(
                revenue, "VND",
                dto.getStart(), dto.getEnd()
        ));
    }


    // --- 1. Verify password ---
    @PostMapping("/verify-password")
    public ResponseEntity<Map<String, Boolean>> verifyPassword(@RequestBody Map<String, String> body, Principal principal) {
        String password = body.get("password");
        boolean valid = revenueService.verifyPassword(principal.getName(), password);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}
