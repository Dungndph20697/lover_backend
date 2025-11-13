package com.codegym.controller;

import com.codegym.model.CcdvServiceDetail;
import com.codegym.service.UserService;
import com.codegym.service.impl.CcdvServiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ccdv/ccdv-service-details")
public class CcdvServiceDetailController {
    @Autowired
    private CcdvServiceDetailService serviceDetailService;

    @Autowired
    private UserService userService;

    @PostMapping("/save/{userId}")
    public ResponseEntity<?> saveUserServices(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            List<Long> serviceIds = ((List<?>) payload.get("serviceIds"))
                    .stream()
                    .map(id -> Long.valueOf(id.toString()))
                    .toList();

            serviceDetailService.saveServicesForUser(userId, serviceIds);
            return ResponseEntity.ok("Dịch vụ đã được lưu thành công (bao gồm cả dịch vụ cơ bản mặc định).");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi lưu dịch vụ: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserServices(@PathVariable Long userId) {
        try {
            List<CcdvServiceDetail> details = serviceDetailService.getServicesByUser(userId);
            if (details.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
        }
    }


}
