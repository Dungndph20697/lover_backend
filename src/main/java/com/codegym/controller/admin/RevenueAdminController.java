package com.codegym.controller.admin;

import com.codegym.model.Revenue;
import com.codegym.repository.HireSessionRepository;
import com.codegym.service.RevenueAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/revenues")
@RequiredArgsConstructor
public class RevenueAdminController {

    private final RevenueAdminService revenueAdminService;
    private final HireSessionRepository hireSessionRepository;

    @GetMapping
    public Page<Revenue> getAllRevenues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return revenueAdminService.getAllRevenues(page, size);
    }

    @GetMapping("/total/{ccdvId}")
    public ResponseEntity<Double> getTotalByCcdv(@PathVariable Long ccdvId) {
        Double total = revenueAdminService.getTotalRevenueByCcdvId(ccdvId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long pending = hireSessionRepository.countByStatus("PENDING");
        long accepted = hireSessionRepository.countByStatus("ACCEPTED");
        long completed = hireSessionRepository.countByStatus("COMPLETED");
        long reviewReport = hireSessionRepository.countByStatus("REVIEW_REPORT");

        return ResponseEntity.ok(Map.of(
                "PENDING", pending,
                "ACCEPTED", accepted,
                "COMPLETED", completed,
                "REVIEW_REPORT", reviewReport
        ));
    }
}